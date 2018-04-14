package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.{ActorComponent, ViewModelComponents}

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
			val actorBox = painter.preRender(actor)

			val actorTopLeft = previousActorDistanceOrDefault(pointMap, actor)
			val actorTopRight = actorTopLeft.right(actorBox.width)
			val actorBottomMiddle = actorTopLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

			pointMap.put(Actor.topLeft(actor.id), actorTopLeft)
			pointMap.put(Actor.topRight(actor.id), actorTopRight)
			pointMap.put(Actor.bottomMiddle(actor.id), actorBottomMiddle)

			for (activity <- actor.activities) {
				if (activity.id == 0) {

					val activityTopLeft = actorBottomMiddle.left(painter.preRender(activity).halfWidth)
					val activityTopRight = activityTopLeft.right(painter.preRender(activity).width)

					pointMap.put(Activity.topLeft(actor.id, activity.id), activityTopLeft)
					pointMap.put(Activity.topRight(actor.id, activity.id), activityTopRight)

					for (point <- activity.rightPoints.values) {
						pointMap.put(Activity.rightPointStart(actor.id, activity.id, point.signalComponent.currentIndex()),
							Fixed2DPoint(activityTopRight.x +1, activityTopRight.y + 1))

						pointMap.put(Activity.rightPointEnd(actor.id, activity.id, point.signalComponent.currentIndex()),
							Fixed2DPoint(activityTopRight.x +1, activityTopRight.y + 1 + painter.preRender(point.signalComponent).height))
					}

					for (point <- activity.leftPoints.values) {
						pointMap.put(Activity.leftPointStart(actor.id, activity.id, point.signalComponent.currentIndex()),
							Fixed2DPoint(activityTopLeft.x, activityTopLeft.y + 1))

						pointMap.put(Activity.leftPointEnd(actor.id, activity.id, point.signalComponent.currentIndex()),
							Fixed2DPoint(activityTopLeft.x, activityTopRight.y + 1 + painter.preRender(point.signalComponent).height))
					}


					val allPoint = activity.rightPoints ++ activity.leftPoints
					val maxActivityPoint = allPoint.maxBy(_._2.signalComponent.currentIndex())._2

					pointMap.put(Activity.bottomLeft(actor.id, activity.id),
						Fixed2DPoint(activityTopLeft.x,
							pointMap(Activity.pointEnd(actor.id, activity.id, maxActivityPoint.id, maxActivityPoint.direction)).y
						)
					)
				}
			}
		}
	}


	private def previousActorDistanceOrDefault(pointMap: PointMap, actor: ActorComponent) = {
		if (actor.id == 0)
			Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN)
		else
			pointMap(Actor.topRight(actor.id - 1)).right(DISTANCE_BETWEEN_ACTORS)
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

		def rightPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "right")

		def leftPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "left")

		def pointStart(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_start"

		def rightPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "right")

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"
	}

}


