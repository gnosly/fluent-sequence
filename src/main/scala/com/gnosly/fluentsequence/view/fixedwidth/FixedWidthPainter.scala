package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.model._

class FixedWidthPainter {
	def preRender(signalComponent: SignalComponent) = signalComponent match {
		case x: AutoSignalComponent => Box(x.name.length + 6, 4)
		case x: BiSignalComponent => Box(x.name.length + 5, 2)
	}

	def preRender(activity: ActivityComponent): Box = Box(2, 2)

	def preRender(actorComponent: ActorComponent): Box = {
		Box(s"| ${actorComponent.name} |".length, 4)
	}

	def paint(actor: ActorComponent,
						pointMap: Map[String, Fixed2DPoint],
						canvas: FixedWidthCanvas): Unit = {
		import Util._

		val padding = 2
		val name = actor.name
		val innerSize = name.length + padding

		val topLeftCornerId = pointMap(Actor.topLeft(actor.id))

		val str = r("-", innerSize)

		canvas.write(topLeftCornerId, "." + str + ".")
		canvas.write(topLeftCornerId.down(1), "| " + name + " |")
		canvas.write(topLeftCornerId.down(2), "'" + str + "'")
		canvas.write(topLeftCornerId.down(3).right(innerSize / 2), "|")


		for (activity <- actor.activities) {

			val topLeftActivity = pointMap(Activity.topLeft(actor.id, activity.id))
			val bottomLeftActivity = pointMap(Activity.bottomLeft(actor.id, activity.id))

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
					case x: AutoSignalComponent => paintAutoSignal(activity.id, x, pointMap, actor, canvas)
					case x: BiSignalComponent => paintBiSignal(activity.id, x, pointMap, actor, canvas)
				}
			}
		}
	}

	private def paintAutoSignal(activityId: Int, x: AutoSignalComponent, pointMap: Map[String, Fixed2DPoint], actor: ActorComponent, canvas: FixedWidthCanvas) = {
		val signalPoint = pointMap(Activity.rightPointStart(actor.id, activityId, x.currentIndex()))

		canvas.write(signalPoint, "____")
		canvas.write(signalPoint.down(1), "    |")
		canvas.write(signalPoint.down(2), "    | " + x.name)
		canvas.write(signalPoint.down(3), "<---'")
	}

	private def paintBiSignal(activityId: Int, x: BiSignalComponent, pointMap: Map[String, Fixed2DPoint], actor: ActorComponent, canvas: FixedWidthCanvas) = {
		val signalPoint = pointMap(Activity.rightPointStart(actor.id, activityId, x.currentIndex()))

		val minTextPosition:Long = x.name.length + 4L
		//TODO implementare activity id
		val leftActivityPoint = pointMap(Activity.leftPointStart(x.toActorId, 0, x.currentIndex()))
		val distance = Math.max(minTextPosition, leftActivityPoint.x - signalPoint.x - 1)

		canvas.write(signalPoint.right((distance-x.name.length)/2), x.name)
		canvas.write(signalPoint.down(1), Util.r("-",distance) + ">")
	}
}

case class Box(width: Long, height: Long) {
	def halfWidth(): Long = width / 2

}