package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.{ActivityPoint, AutoSignalComponent, BiSignalComponent, ViewModelComponents}

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
					pointMap.put(topLeftCornerIdForActivity(actor.id, activity.id),
						pointMap(bottomMiddleCornerIdForActor(actor.id)).left(painter.preRender(activity)))
					for (point <- activity.rightPoints.values) {
						point match {
							case ActivityPoint(pointId, autoSignal: AutoSignalComponent)  => {
								pointMap.put("actor_0_activity_0_right_point_0", Fixed2DPoint(6, 6))
								pointMap.put("actor_0_activity_0_right_point_1", Fixed2DPoint(6, 9))
							}
							case ActivityPoint(pointId, bisignalComponent: BiSignalComponent) => {
								pointMap.put("actor_0_activity_0_right_point_0", Fixed2DPoint(6, 6))
							}
						}

					}

					for (point <- activity.leftPoints.values) {
						point match {
							case ActivityPoint(pointId, _) => {
								pointMap.put(pointForActivity(actor.id
									, activity.id, pointId, "left"),
									Fixed2DPoint(19, 6))
							}
						}

					}
				}
			}
		}
	}

	private def topLeftCornerIdForActor(column: Int) = s"actor_${column}_top_left"

	private def topRightCornerIdForActor(column: Int) = s"actor_${column}_top_right"

	private def bottomMiddleCornerIdForActor(column: Int): String = s"actor_${column}_bottom_middle"

	private def topLeftCornerIdForActivity(column: Int, id: Int): String = s"actor_${column}_activity_${id}_top_left"

	private def pointForActivity(actor: Int, activity: Int, point: Int, direction: String): String = s"actor_${actor}_activity_${activity}_${direction}_point_${point}"

}


