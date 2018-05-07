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

	def paint(actor: ActorComponent,
						pointMap: Map[String, Fixed2DPoint],
						canvas: FixedWidthCanvas): Unit = {
		import Util._

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

			timelineStart = Fixed2DPoint(timelineStart.x, bottomLeftActivity.down(2).y)
		}
	}

	private def paintActivity(pointMap: Map[String, Fixed2DPoint], canvas: FixedWidthCanvas, timelineStart: Fixed2DPoint, activity: ActivityComponent) = {
		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

		canvas.write(topLeftActivity, "_|_")

		val activityStart = topLeftActivity.down(1)
		for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y) {
			canvas.write(activityStart.down(i), "| |")
		}

		canvas.write(bottomLeftActivity, "|_|")
		canvas.write(bottomLeftActivity.down(1).right(1), "|")

		//print right signal
		for (rightPoint <- activity.rightPoints) {
			rightPoint._2.signalComponent match {
				case x: AutoSignalComponent => paintAutoSignal(activity.id, x, pointMap, canvas)
				case x: BiSignalComponent => paintBiSignal(x, pointMap, canvas)
			}
		}
	}

	private def paintAutoSignal(activityId: Int, x: AutoSignalComponent, pointMap: Map[String, Fixed2DPoint], canvas: FixedWidthCanvas) = {
		val signalPoint = pointMap(Activity.rightPointStart(x.actorId, activityId, x.currentIndex()))

		canvas.write(signalPoint, "____")
		canvas.write(signalPoint.down(1), "    |")
		canvas.write(signalPoint.down(2), "    | " + x.name)
		canvas.write(signalPoint.down(3), "<---'")
	}

	private def paintBiSignal(s: BiSignalComponent, pointMap: Map[String, Fixed2DPoint], canvas: FixedWidthCanvas) = {


		val minTextPosition: Long = s.name.length + 4L
		if (s.leftToRight()) {
			val signalPoint = pointMap(Activity.rightPointStart(s.fromActorId, s.fromActivityId, s.currentIndex()))
			val leftActivityPoint = pointMap(Activity.leftPointStart(s.toActorId, s.toActivityId, s.currentIndex()))
			val distance = Math.max(minTextPosition, leftActivityPoint.x - signalPoint.x - 1)

			canvas.write(signalPoint.right((distance - s.name.length) / 2), s.name)
			canvas.write(signalPoint.down(1), Util.r("-", distance) + ">")
		} else {
			val signalLeftPoint = pointMap(Activity.rightPointStart(s.toActorId, 0, s.currentIndex()))
			val signalRightPoint = pointMap(Activity.leftPointStart(s.fromActorId, 0, s.currentIndex()))
			val distance = Math.max(minTextPosition, signalRightPoint.x - signalLeftPoint.x - 1)

			canvas.write(signalLeftPoint.right((distance - s.name.length) / 2), s.name)
			canvas.write(signalLeftPoint.down(1), "<" + Util.r("-", distance))
		}
	}
}

case class Box(width: Long, height: Long) {
	def halfWidth(): Long = width / 2

}