package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, Fixed2DPoint] = {
		val pointMap = new PointMap
		val columnWidth = new SingleSize()
		val rowHeight = new SingleSize()

		while (true) {
			val previousPointMap = pointMap.toMap().toMap
			formatIteration(viewModel, pointMap, columnWidth, rowHeight)
			if (pointMap.toMap().toMap == previousPointMap) {
				return pointMap.toMap()
			}
		}

		pointMap.toMap()
	}


	private def formatIteration(viewModel: ViewModelComponents, pointMap: PointMap, columnWidth: SingleSize, rowHeight: SingleSize) = {
		for (actor <- viewModel._actors.values) {
			columnWidth.updateMax(actor.id, DISTANCE_BETWEEN_ACTORS)

			val actorPoints = formatActor(pointMap, columnWidth, actor)
			pointMap.putAll(actorPoints.toPoints())

			val actorBottomMiddle: Fixed2DPoint = actorPoints.actorBottomMiddle

			for (activity <- actor.activities) {
				formatActivity(actorBottomMiddle, pointMap, columnWidth, rowHeight, activity)
			}
		}
	}

	private def formatActivity(actorBottomMiddle: Fixed2DPoint,
														 pointMap: PointMap,
														 columnWidth: SingleSize,
														 rowHeight: SingleSize,
														 activity: ActivityComponent) = {

		val activityBox = painter.preRender(activity)


		var activityY = 0L

		if (activity.fromIndex > 1) {
			activityY = rowHeight(activity.fromIndex - 1)
		} else {
			activityY = actorBottomMiddle.y
		}

		val activityTopLeft = actorBottomMiddle.left(activityBox.halfWidth).atY(activityY)
		val activityTopRight = activityTopLeft.right(activityBox.width)

		val actorId = activity.actorId

		formatSignals(actorId, pointMap, columnWidth, rowHeight, activity, activityTopLeft, activityTopRight, activity.rightPoints, "right")
		formatSignals(actorId, pointMap, columnWidth, rowHeight, activity, activityTopLeft, activityTopRight, activity.leftPoints, "left")

		val lastPoint = rowHeight(activity.toIndex)

		pointMap.putAll(
			new ActivityPoints(actorId, activity.id, actorBottomMiddle, activityBox, activityY, lastPoint).toPoints()
		)
	}

	private def formatSignals(actorId: Int, pointMap: PointMap, columnWidth: SingleSize, rowHeight: SingleSize, activity: ActivityComponent, activityTopLeft: Fixed2DPoint, activityTopRight: Fixed2DPoint, rightPoints: mutable.TreeMap[Int, ActivityPoint], direction: String) = {
		for (point <- rightPoints) {
			val signal = point._2.signalComponent
			formatSignal(actorId, pointMap, columnWidth, rowHeight, activity, activityTopLeft, activityTopRight, signal, direction)
		}
	}

	private def formatSignal(actorId: Int, pointMap: PointMap, columnWidth: SingleSize, rowHeight: SingleSize, activity: ActivityComponent, activityTopLeft: Fixed2DPoint, activityTopRight: Fixed2DPoint, signal: SignalComponent, direction: String) = {
		def previousIndexPointOrDefault(): Long = {
			if (signal.currentIndex() == 1) {
				return activityTopLeft.y + 1
			} else {
				return rowHeight(signal.currentIndex() - 1) + DISTANCE_BETWEEN_SIGNALS
			}
		}

		val distanceBetweenSignals = previousIndexPointOrDefault()


		val signalBox = painter.preRender(signal)
		columnWidth.updateMax(actorId, signalBox.width)

		rowHeight.updateMax(signal.currentIndex(), distanceBetweenSignals + signalBox.height)


		pointMap.putAll(
			new SignalPoints(actorId, activity.id, signal.currentIndex(), signalBox, distanceBetweenSignals, direction, activityTopRight, activityTopLeft).toPoints()
		)

	}

	private def formatActor(pointMap: PointMap,
													columnWidth: SingleSize,
													actor: ActorComponent) = {
		def previousActorDistanceOrDefault() = {
			if (actor.id == 0)
				Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN)
			else
				pointMap(Actor.topRight(actor.id - 1)).right(Math.max(columnWidth(actor.id - 1), DISTANCE_BETWEEN_ACTORS))
		}

		val actorTopLeft = previousActorDistanceOrDefault()

		val actorBox = painter.preRender(actor)
		new ActorPoints(actor.id, actorTopLeft, actorBox)
	}


}

class SingleSize(intervals: mutable.TreeMap[Int, Long] = mutable.TreeMap[Int, Long]()) {
	def updateMax(interval: Int, size: Long): Unit = {
		if (intervals.contains(interval)) {
			if (size > intervals(interval)) {
				intervals.put(interval, size)
			}
		} else {
			intervals.put(interval, size)
		}
	}

	def apply(column: Int): Long = intervals.getOrElse(column, 0)
}

object Coordinates {

	class ActorPoints(actorId: Int, topLeft: Fixed2DPoint, actorBox: Box) {
		val actorTopRight = topLeft.right(actorBox.width)
		val actorBottomMiddle = topLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Actor.topLeft(actorId) -> topLeft ::
				Actor.topRight(actorId) -> actorTopRight ::
				Actor.bottomMiddle(actorId) -> actorBottomMiddle :: Nil

		}
	}

	class ActivityPoints(actorId: Int, activityId: Int, actorBottomMiddle: Fixed2DPoint, activityBox: Box, activityYStart: Long, activityYEnd: Long) {
		val activityTopLeft = actorBottomMiddle.left(activityBox.halfWidth).atY(activityYStart)
		val activityTopRight = activityTopLeft.right(activityBox.width)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Activity.topLeft(actorId, activityId) -> activityTopLeft ::
				Activity.topRight(actorId, activityId) -> activityTopRight ::
				Activity.bottomLeft(actorId, activityId) -> Fixed2DPoint(activityTopLeft.x, activityYEnd) :: Nil
		}
	}

	class SignalPoints(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box, distanceBetweenSignals: Long, direction: String, activityTopRight: Fixed2DPoint, activityTopLeft: Fixed2DPoint) {
		val signalXStart = if (direction.equals("right")) activityTopRight.x + 1 else activityTopLeft.x
		val fixedPointEnd = Fixed2DPoint(signalXStart, distanceBetweenSignals + signalBox.height)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> Fixed2DPoint(signalXStart, distanceBetweenSignals) ::
				Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd :: Nil
		}
	}

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

		def pointStart(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_start"

		def leftPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "left")

		def rightPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "right")

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

	}

}




