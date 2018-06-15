package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.model._

class FixedWidthPainter {
	def preRender(actorComponent: ActorComponent): Box = {
		Box(s"| ${actorComponent.name} |".length, 4)
	}

	def preRender(activity: ActivityComponent): Box = Box(2, 2)

	def preRender(signalComponent: SignalComponent) = signalComponent match {
		case x: AutoSignalComponent => Box(x.name.length + 6, 4)
		case x: BiSignalComponent => Box(x.name.length + 5, 2)
	}

	def paint(viewModelComponents: ViewModelComponents, pointMap: Map[String, Fixed2dPoint], canvas: FixedWidthCanvas): Unit = {
		val sequenceWidth = allColumnWidth(viewModelComponents, pointMap)
		val sequenceHeight = pointMap(ViewMatrix.row(viewModelComponents.lastIndex)).x + 3

		canvas.write(Fixed2dPoint(0, 0), r("_", viewModelComponents.sequenceName.size + 3))
		canvas.write(Fixed2dPoint(0, 1), s"| ${viewModelComponents.sequenceName} \\")
		canvas.write(Fixed2dPoint(0, 2), "|" + r("-", sequenceWidth))
		canvas.write(Fixed2dPoint(0, sequenceHeight), "|" + r("_", sequenceWidth))

		for (y <- 3 until sequenceHeight.toInt) {
			canvas.write(Fixed2dPoint(0, y), "|")
			canvas.write(Fixed2dPoint(sequenceWidth, y), "|")
		}
		viewModelComponents._actors.foreach(
			a => paint(a._2, pointMap, canvas)
		)
	}

	private def allColumnWidth(viewModelComponents: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Long = {
		val sequenceWidth = 3
		val count = viewModelComponents._actors.foldLeft(0L)((z, a) => {
			z + sequenceWidth + pointMap(ViewMatrix.column(a._2.id)).x
		})

		FormatterConstants.LEFT_MARGIN + count
	}

	def paint(actor: ActorComponent, pointMap: Map[String, Fixed2dPoint], canvas: FixedWidthCanvas): Unit = {
		val padding = 2
		val name = actor.name
		val innerSize = name.length + padding

		val actorTopLeft = pointMap(Actor.topLeft(actor.id))

		val str = r("-", innerSize)

		canvas.write(actorTopLeft, "." + str + ".")
		canvas.write(actorTopLeft.down(1), "| " + name + " |")
		canvas.write(actorTopLeft.down(2), "'" + str + "'")
		canvas.write(actorTopLeft.down(3).right(innerSize / 2), "|")


		var timelineStart = pointMap(Actor.bottomMiddle(actor.id))

		for (activity <- actor.activities) {
			val topLeftActivity = pointMap(Activity.topLeft(actor.id, activity.id))
			val bottomLeftActivity = pointMap(Activity.bottomLeft(actor.id, activity.id))

			0L until topLeftActivity.y - timelineStart.y foreach (i => canvas.write(timelineStart.down(i), "|"))

			paintActivity(pointMap, canvas, timelineStart, activity)

			timelineStart = new Fixed2dPoint(timelineStart.x, bottomLeftActivity.down(1).y)
		}
		canvas.write(timelineStart, "|")
	}

	private def paintActivity(pointMap: Map[String, Fixed2dPoint], canvas: FixedWidthCanvas, timelineStart: Fixed2dPoint, activity: ActivityComponent) = {
		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

		canvas.write(topLeftActivity, "_|_")

		val activityStart = topLeftActivity.down(1)
		for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y) {
			canvas.write(activityStart.down(i), "| |")
		}

		canvas.write(bottomLeftActivity, "|_|")
		//canvas.write(bottomLeftActivity.down(1).right(1), "|")

		//print right signal
		for (rightPoint <- activity.rightPoints) {
			rightPoint._2.signalComponent match {
				case x: AutoSignalComponent => paintAutoSignal(activity.id, x, pointMap, canvas)
				case x: BiSignalComponent => paintBiSignal(x, pointMap, canvas)
			}
		}
	}

	private def paintAutoSignal(activityId: Int, x: AutoSignalComponent, pointMap: Map[String, Fixed2dPoint], canvas: FixedWidthCanvas) = {
		val signalPoint = pointMap(Activity.rightPointStart(x.actorId, activityId, x.currentIndex()))

		canvas.write(signalPoint, "____")
		canvas.write(signalPoint.down(1), "    |")
		canvas.write(signalPoint.down(2), "    | " + x.name)
		canvas.write(signalPoint.down(3), "<---'")
	}

	private def paintBiSignal(s: BiSignalComponent, pointMap: Map[String, Fixed2dPoint], canvas: FixedWidthCanvas) = {
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

	def r(pattern: String, count: Long): String =
		(0 until count.toInt).map(_ => pattern).reduce(_ + _)
}

case class Box(width: Long, height: Long) {
	def halfWidth(): Long = width / 2

}