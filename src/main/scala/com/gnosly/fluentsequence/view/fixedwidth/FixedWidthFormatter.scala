package com.gnosly.fluentsequence.view.fixedwidth

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
				pointMap.put(topLeftCornerIdForActor(actor.id), Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN))
			} else {
				pointMap.put(topLeftCornerIdForActor(actor.id),
					pointMap(topRightCornerIdForActor(actor.id - 1)).right(DISTANCE_BETWEEN_ACTORS))
			}

			pointMap.put(topRightCornerIdForActor(actor.id),
				pointMap(topLeftCornerIdForActor(actor.id)).right(painter.preRender(actor).x))

			val actorBox = painter.preRender(actor)

			pointMap.put(bottomMiddleCornerIdForActor(actor.id),
				pointMap(topLeftCornerIdForActor(actor.id))
					.right((actorBox.x - 1) / 2)
					.down(actorBox.y)
			)

			for (activity <- actor.activities) {
				if (activity.id == 0) {
					val actorBottomMiddle = pointMap(bottomMiddleCornerIdForActor(actor.id))
					val activityTopLeft = actorBottomMiddle.left(painter.preRender(activity))
					val activityTopRight = actorBottomMiddle.right(painter.preRender(activity))

					pointMap.put(topLeftCornerIdForActivity(actor.id, activity.id), activityTopLeft)
					pointMap.put(topRightCornerIdForActivity(actor.id, activity.id),activityTopRight)

					for (point <- activity.rightPoints.values) {
								val activityTopRight = pointMap(topRightCornerIdForActivity(actor.id, activity.id))
								pointMap.put(pointForActivity(actor.id, activity.id, point.id, "right"),
									Fixed2DPoint(activityTopRight.x, 6))
					}

					for (point <- activity.leftPoints.values) {
								pointMap.put(pointForActivity(actor.id, activity.id, point.id, "left"),
									Fixed2DPoint(activityTopLeft.x, 6))
					}
				}
			}
		}
	}

	private def topLeftCornerIdForActor(column: Int) = s"actor_${column}_top_left"

	private def topRightCornerIdForActor(column: Int) = s"actor_${column}_top_right"

	private def bottomMiddleCornerIdForActor(column: Int): String = s"actor_${column}_bottom_middle"

	private def topLeftCornerIdForActivity(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_top_left"

	private def topRightCornerIdForActivity(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_top_right"

	private def pointForActivity(actor: Int, activity: Int, point: Int, direction: String): String = s"actor_${actor}_activity_${activity}_${direction}_point_${point}"

}


