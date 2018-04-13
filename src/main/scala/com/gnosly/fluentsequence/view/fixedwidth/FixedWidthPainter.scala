package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent}

class FixedWidthPainter {
	def preRender(activity: ActivityComponent):Box = Box(2,2)

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

		val topLeftCornerId = pointMap(topLeftCornerIdForActor(actor.id))

		val str = r("-", innerSize)

		canvas.write(topLeftCornerId, "." + str + ".")
		canvas.write(topLeftCornerId.down(1), "| " + name + " |")
		canvas.write(topLeftCornerId.down(2), "'" + str + "'")
		canvas.write(topLeftCornerId.down(3).right(innerSize / 2), "|")


		for(activity <- actor.activities){

			val topLeftActivity = pointMap(topLeftCornerIdForActivity(actor.id, activity.id))
			val bottomLeftActivity = pointMap(bottomLeftCornerIdForActivity(actor.id, activity.id))

			canvas.write(topLeftActivity, "_|_")

			val activityStart = topLeftActivity.down(1)
			for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y ){
				canvas.write(activityStart.down(i), "| |")
			}

				canvas.write(bottomLeftActivity, "|_|")
				canvas.write(bottomLeftActivity.down(1).right(1),  "|")

		}
	}
}

case class Box(width: Long, height: Long) {
	def halfWidth(): Long =  width/2

}