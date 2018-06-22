package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.Canvas
import com.gnosly.fluentsequence.view.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2dPoint, FixedWidthCanvas, FormatterConstants}
import com.gnosly.fluentsequence.view.model._
import com.gnosly.fluentsequence.view.model.component._

class FixedWidthPainter {
  def preRender(actorComponent: ActorComponent): Box = {
    Box(s"| ${actorComponent.name} |".length, 4)
  }

  def preRender(activity: ActivityComponent): Box = Box(2, 2)

  def preRender(signalComponent: SignalComponent) = signalComponent match {
    case x: AutoSignalComponent => Box(x.name.length + 6, 4)
    case x: BiSignalComponent => Box(x.name.length + 5, 2)
  }

  val actorPainter = new FixedWidthActorPainter()

  def paint(viewModelComponents: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas = {
    val canvas = new FixedWidthCanvas()
    val sequenceWidth = allColumnWidth(viewModelComponents, pointMap)
    val sequenceHeight = pointMap(ViewMatrix.row(viewModelComponents.lastSignalIndex)).x + 3

    val component = viewModelComponents.sequenceComponents(0)
    val sequenceTitle = component.name
    canvas.write(Fixed2dPoint(0, 0), r("_", sequenceTitle.length + 3))
    canvas.write(Fixed2dPoint(0, 1), s"| ${sequenceTitle} \\")
    canvas.write(Fixed2dPoint(0, 2), "|" + r("-", sequenceWidth))
    canvas.write(Fixed2dPoint(0, sequenceHeight), "|" + r("_", sequenceWidth))

    for (y <- 3 until sequenceHeight.toInt) {
      canvas.write(Fixed2dPoint(0, y), "|")
      canvas.write(Fixed2dPoint(sequenceWidth, y), "|")
    }
    viewModelComponents._actors.foreach(
      a => actorPainter.paint(a._2, canvas, pointMap)
    )

    viewModelComponents._actors.flatMap(_._2.activities).foreach(
      a => {

        if (a.id == 0) {
          val timelineStart = pointMap(Actor.bottomMiddle(a.actorId))
          val topLeftActivity = pointMap(Activity.topLeft(a.actorId, a.id))
          0L until topLeftActivity.y - timelineStart.y foreach (i => canvas.write(timelineStart.down(i), "|"))
        } else {
          val previousBottomLeftActivity = pointMap(Activity.bottomLeft(a.actorId, a.id - 1))
          val topLeftActivity = pointMap(Activity.topLeft(a.actorId, a.id))

          1L until topLeftActivity.y - previousBottomLeftActivity.y foreach (i => canvas.write(previousBottomLeftActivity.down(i), "|"))
        }

        val existNextActivity = pointMap.contains(Activity.bottomLeft(a.actorId, a.id + 1))
        if (!existNextActivity) { // not exist //todo
          val bottomLeft = pointMap(Activity.bottomLeft(a.actorId, a.id))
          canvas.write(bottomLeft.down(1).right(1), "|")
        }

        paintActivity(a, canvas, pointMap)
      }
    )

    return canvas
  }

  private def paintActivity(activity: ActivityComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
    val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
    val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

    canvas.write(topLeftActivity, "_|_")

    val activityStart = topLeftActivity.down(1)
    for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y) {
      canvas.write(activityStart.down(i), "| |")
    }

    canvas.write(bottomLeftActivity, "|_|")

    //print right signal
    for (rightPoint <- activity.rightPoints) {
      rightPoint._2.signalComponent match {
        case x: AutoSignalComponent => paintAutoSignal(x, canvas, pointMap)
        case x: BiSignalComponent => paintBiSignal(x, canvas, pointMap)
      }
    }
  }

  private def paintAutoSignal(x: AutoSignalComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
    val signalPoint = pointMap(Activity.rightPointStart(x.actorId, x.activityId, x.currentIndex()))

    canvas.write(signalPoint, "____")
    canvas.write(signalPoint.down(1), "    |")
    canvas.write(signalPoint.down(2), "    | " + x.name)
    canvas.write(signalPoint.down(3), "<---'")
  }

  private def paintBiSignal(s: BiSignalComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
    if (s.leftToRight()) {
      val signalPoint = pointMap(Activity.rightPointStart(s.fromActorId, s.fromActivityId, s.currentIndex()))
      val leftActivityPoint = pointMap(Activity.leftPointStart(s.toActorId, s.toActivityId, s.currentIndex()))
      val distance = leftActivityPoint.x - signalPoint.x

      canvas.write(signalPoint.right((distance - s.name.length) / 2), s.name)
      canvas.write(signalPoint.down(1), r("-", distance - 1) + ">")
    } else {
      val signalLeftPoint = pointMap(Activity.rightPointStart(s.toActorId, s.toActivityId, s.currentIndex()))
      val signalRightPoint = pointMap(Activity.leftPointStart(s.fromActorId, s.fromActivityId, s.currentIndex()))
      val distance = signalRightPoint.x - signalLeftPoint.x

      canvas.write(signalLeftPoint.right((distance - s.name.length) / 2), s.name)
      canvas.write(signalLeftPoint.down(1), "<" + r("-", distance - 1))
    }
  }

  private def allColumnWidth(viewModelComponents: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Long = {
    val sequenceWidth = 3
    val count = viewModelComponents._actors.foldLeft(0L)((z, a) => {
      z + sequenceWidth + pointMap(ViewMatrix.column(a._2.id)).x
    })

    FormatterConstants.LEFT_MARGIN + count
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)
}

case class Box(width: Long, height: Long) {
  def halfWidth(): Long = width / 2

}