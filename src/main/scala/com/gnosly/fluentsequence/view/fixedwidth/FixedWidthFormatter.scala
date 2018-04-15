package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.{ActorComponent, SignalComponent, ViewModelComponents}

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

					var yy = 0L
					if(activity.fromIndex > 1){
						yy = pointMap(Coordinates.endOfIndex(activity.fromIndex - 1)).y - actorBottomMiddle.y
					}

					val activityTopLeft = actorBottomMiddle.left(painter.preRender(activity).halfWidth).down(yy)
					val activityTopRight = activityTopLeft.right(painter.preRender(activity).width)

					pointMap.put(Activity.topLeft(actor.id, activity.id), activityTopLeft)
					pointMap.put(Activity.topRight(actor.id, activity.id), activityTopRight)

					for (point <- activity.rightPoints) {
						val signal = point._2.signalComponent
						val distanceBetweenSignals = previousIndexPointOrDefault(pointMap, signal, activityTopLeft.y)

						pointMap.put(Activity.rightPointStart(actor.id, activity.id, signal.currentIndex()),
							Fixed2DPoint(activityTopRight.x + 1, distanceBetweenSignals))

						val fixedPointEnd: Fixed2DPoint = Fixed2DPoint(activityTopRight.x + 1, distanceBetweenSignals + painter.preRender(signal).height)
						pointMap.put(Activity.rightPointEnd(actor.id, activity.id, signal.currentIndex()), fixedPointEnd)
						pointMap.put(Coordinates.endOfIndex(signal.currentIndex()), fixedPointEnd)
					}

					for (point <- activity.leftPoints) {
						val signal = point._2.signalComponent
						val distanceBetweenSignals = previousIndexPointOrDefault(pointMap, signal, activityTopLeft.y)

						pointMap.put(Activity.leftPointStart(actor.id, activity.id, signal.currentIndex()),
							Fixed2DPoint(activityTopLeft.x, distanceBetweenSignals))

						val  fixedPointEnd = Fixed2DPoint(activityTopLeft.x, distanceBetweenSignals + painter.preRender(signal).height)
						pointMap.put(Activity.leftPointEnd(actor.id, activity.id, signal.currentIndex()),fixedPointEnd)
						pointMap.put(Coordinates.endOfIndex(signal.currentIndex()), fixedPointEnd)
					}


					val lastPoint = pointMap(endOfIndex(activity.toIndex))

					pointMap.put(Activity.bottomLeft(actor.id, activity.id), Fixed2DPoint(activityTopLeft.x, lastPoint.y))
				}
			}
		}
	}

	private def previousIndexPointOrDefault(pointMap: PointMap, signal: SignalComponent, activityTop: Long): Long = {
		if (signal.currentIndex() == 1) {
			return activityTop + 1
		} else {
			return pointMap(Coordinates.endOfIndex(signal.currentIndex() - 1)).y + DISTANCE_BETWEEN_SIGNALS
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

	def endOfIndex(index: Int): String = s"index_${index}_end"
}


