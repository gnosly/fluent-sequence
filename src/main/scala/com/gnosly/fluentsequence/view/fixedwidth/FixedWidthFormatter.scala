package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.ViewModelComponents

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, Fixed2DPoint] = {
		val pointMap = new PointMap

		while (true) {
			val previousPointMap = pointMap.toMap().toMap
			formatIteration(viewModel, pointMap)
			if (pointMap.toMap().toMap == previousPointMap) {
				return pointMap.toMap()
			}
		}

		pointMap.toMap()
	}

	private def formatIteration(viewModel: ViewModelComponents, pointMap: PointMap) = {
		for (actor <- viewModel._actors.values) {
			if (actor.id == 0) {
				pointMap.put(Actor.topLeft(actor.id), Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN))
			} else {
				pointMap.put(Actor.topLeft(actor.id),
					pointMap(Actor.topRight(actor.id - 1)).right(DISTANCE_BETWEEN_ACTORS))
			}

			pointMap.put(Actor.topRight(actor.id),
				pointMap(Actor.topLeft(actor.id)).right(painter.preRender(actor).width))

			val actorBox = painter.preRender(actor)

			pointMap.put(Actor.bottomMiddle(actor.id),
				pointMap(Actor.topLeft(actor.id))
					.right((actorBox.width - 1) / 2)
					.down(actorBox.height)
			)

			val actorBottomMiddle = pointMap(Actor.bottomMiddle(actor.id))

			for (activity <- actor.activities) {
				if (activity.id == 0) {

					val activityTopLeft = actorBottomMiddle.left(painter.preRender(activity).halfWidth)
					val activityTopRight = actorBottomMiddle.right(painter.preRender(activity).halfWidth)

					pointMap.put(Activity.topLeft(actor.id, activity.id), activityTopLeft)
					pointMap.put(Activity.topRight(actor.id, activity.id), activityTopRight)

					for (point <- activity.rightPoints.values) {
						pointMap.put(Activity.rightPoint(actor.id, activity.id, point.id),
							Fixed2DPoint(activityTopRight.x, activityTopRight.y + 1))
					}

					for (point <- activity.leftPoints.values) {
						pointMap.put(Activity.leftPoint(actor.id, activity.id, point.id),
							Fixed2DPoint(activityTopLeft.x, activityTopLeft.y + 1))
					}


					//					activity.rightPoints.values.lastOption
					//						.zip(activity.leftPoints.values.lastOption)
					//  						.reduce()

					pointMap.put(Activity.bottomLeft(actor.id, activity.id),
						Fixed2DPoint(activityTopLeft.x, Math.max(
							pointMap(Activity.rightPoint(actor.id, activity.id, activity.rightPoints.last._2.id)).y,
							pointMap(Activity.leftPoint(actor.id, activity.id, activity.leftPoints.last._2.id)).y
						))
					)
				}
			}
		}
	}


}

object Coordinates {

	object Actor {
		def topLeft(actorId: Int) = s"actor_${actorId}_top_left"

		def topRight(actorId: Int) = s"actor_${actorId}_top_right"

		def bottomMiddle(actorId: Int): String = s"actor_${actorId}_bottom_middle"
	}

	object Activity {

		def topLeft(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_top_left"

		def topRight(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_top_right"

		def bottomLeft(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_bottom_left"

		def rightPoint(actorId: Int, activityId: Int, pointId: Int): String = s"actor_${actorId}_activity_${activityId}_right_point_${pointId}"

		def leftPoint(actorId: Int, activityId: Int, pointId: Int): String = s"actor_${actorId}_activity_${activityId}_left_point_${pointId}"
	}

}


