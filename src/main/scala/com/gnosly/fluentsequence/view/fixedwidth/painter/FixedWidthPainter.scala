package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.Coordinates._
import com.gnosly.fluentsequence.view._
import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model._
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class FixedWidthPainter extends Painter {
  val actorPainter = new FixedWidthActorPainter()
  val biSignalPainter = new FixedWidthBiSignalPainter()
  val autoSignalPainter = new FixedWidthAutoSignalPainter()
  val preRenderer = new FixedPreRenderer()
  val activityPainter = new FixedWidthActivityPainter()

  override def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas = {
    val titleCanvas = paintTitle(viewModel, pointMap)

    val actorCanvas = viewModel._actors.map(a => actorPainter.paint(a._2, pointMap))

    val activityCanvas = for {
      a <- viewModel._actors
      activity <- a._2.activities
    } yield activityPainter.paint(activity, pointMap)

    val signalCanvas = for {
      a <- viewModel._actors
      activity <- a._2.activities
      rightPoint <- activity.rightPoints
    } yield rightPoint._2.signalComponent match {
      case x: AutoSignalComponent => autoSignalPainter.paint(x, pointMap)
      case x: BiSignalComponent => biSignalPainter.paint(x, pointMap)
    }

    return (actorCanvas ++ activityCanvas ++ signalCanvas)
      .reduce(_.merge(_))
      .merge(titleCanvas)
  }

  private def paintTitle(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): FixedWidthCanvas = {
    val canvas = new FixedWidthCanvas()
    val sequenceWidth = allColumnWidth(viewModel, pointMap)
    val sequenceHeight = pointMap(ViewMatrix.row(viewModel.lastSignalIndex)).x + 3

    val component = viewModel.sequenceComponents.head
    val sequenceTitle = component.name
    canvas.write(Fixed2dPoint(0, 0), r("_", sequenceTitle.length + 3))
    canvas.write(Fixed2dPoint(0, 1), s"| ${sequenceTitle} \\")
    canvas.write(Fixed2dPoint(0, 2), "|" + r("-", sequenceWidth))
    canvas.write(Fixed2dPoint(0, sequenceHeight), "|" + r("_", sequenceWidth))

    for (y <- 3 until sequenceHeight.toInt) {
      canvas.write(Fixed2dPoint(0, y), "|")
      canvas.write(Fixed2dPoint(sequenceWidth, y), "|")
    }
    canvas
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

  private def allColumnWidth(viewModelComponents: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Long = {
    val sequenceWidth = 3
    //FIXME: move into viewModel
    val count = viewModelComponents._actors.foldLeft(0L)((z, a) => {
      z + sequenceWidth + pointMap(ViewMatrix.column(a._2.id)).x
    })

    FormatterConstants.LEFT_MARGIN + count
  }

}