package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent, SignalComponent, ViewModelComponents}

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

			val actorBottomMiddle: Fixed2DPoint = formatActor(pointMap, columnWidth, actor)

			for (activity <- actor.activities) {
				formatActivity(actor.id, actorBottomMiddle, pointMap, columnWidth, rowHeight, activity)
			}
		}
	}

	private def formatActivity(actorId: Int,
														 actorBottomMiddle: Fixed2DPoint,
														 pointMap: PointMap,
														 columnWidth: SingleSize,
														 rowHeight: SingleSize,
														 activity: ActivityComponent) = {
		var activityTopLeft = Fixed2DPoint(0, 0)
		if (activity.fromIndex > 1) {
			activityTopLeft = actorBottomMiddle
				.left(painter.preRender(activity).halfWidth)
				.atY(rowHeight(activity.fromIndex - 1))
		} else {
			activityTopLeft = actorBottomMiddle.left(painter.preRender(activity).halfWidth)
		}

		val activityTopRight = activityTopLeft.right(painter.preRender(activity).width)

		pointMap.put(Activity.topLeft(actorId, activity.id), activityTopLeft)
		pointMap.put(Activity.topRight(actorId, activity.id), activityTopRight)

		for (point <- activity.rightPoints) {
			val signal = point._2.signalComponent
			val distanceBetweenSignals = previousIndexPointOrDefault(signal, activityTopLeft.y, rowHeight)
			val signalXStart = activityTopRight.x + 1

			pointMap.put(Activity.pointStart(actorId, activity.id, signal.currentIndex(), "right"),
				Fixed2DPoint(signalXStart, distanceBetweenSignals))

			val signalBox = painter.preRender(signal)
			columnWidth.updateMax(actorId, signalBox.width)

			val fixedPointEnd: Fixed2DPoint = Fixed2DPoint(signalXStart, distanceBetweenSignals + signalBox.height)
			pointMap.put(Activity.pointEnd(actorId, activity.id, signal.currentIndex(), "right"), fixedPointEnd)
			rowHeight.updateMax(signal.currentIndex(), distanceBetweenSignals + signalBox.height)
		}

		for (point <- activity.leftPoints) {
			val signal = point._2.signalComponent
			val distanceBetweenSignals = previousIndexPointOrDefault(signal, activityTopLeft.y, rowHeight)
			val signalXStart = activityTopLeft.x

			pointMap.put(Activity.pointStart(actorId, activity.id, signal.currentIndex(), "left"),
				Fixed2DPoint(signalXStart, distanceBetweenSignals))

			val signalBox = painter.preRender(signal)
			columnWidth.updateMax(actorId, signalBox.width)

			val fixedPointEnd = Fixed2DPoint(signalXStart, distanceBetweenSignals + signalBox.height)
			pointMap.put(Activity.pointEnd(actorId, activity.id, signal.currentIndex(), "left"), fixedPointEnd)
			rowHeight.updateMax(signal.currentIndex(), distanceBetweenSignals + signalBox.height)
		}

		val lastPoint = rowHeight(activity.toIndex)

		pointMap.put(Activity.bottomLeft(actorId, activity.id), Fixed2DPoint(activityTopLeft.x, lastPoint))
	}

	private def previousIndexPointOrDefault(signal: SignalComponent, activityTop: Long, rowHeight: SingleSize): Long = {
		if (signal.currentIndex() == 1) {
			return activityTop + 1
		} else {
			return rowHeight(signal.currentIndex() - 1) + DISTANCE_BETWEEN_SIGNALS
		}
	}

	private def formatActor(pointMap: PointMap, columnWidth: SingleSize, actor: ActorComponent) = {
		val actorBox = painter.preRender(actor)
		val actorTopLeft = previousActorDistanceOrDefault(pointMap, actor, columnWidth)
		val actorTopRight = actorTopLeft.right(actorBox.width)
		val actorBottomMiddle = actorTopLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

		pointMap.put(Actor.topLeft(actor.id), actorTopLeft)
		pointMap.put(Actor.topRight(actor.id), actorTopRight)
		pointMap.put(Actor.bottomMiddle(actor.id), actorBottomMiddle)
		actorBottomMiddle
	}

	private def previousActorDistanceOrDefault(pointMap: PointMap, actor: ActorComponent, columnWidth: SingleSize) = {
		if (actor.id == 0)
			Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN)
		else
			pointMap(Actor.topRight(actor.id - 1)).right(Math.max(columnWidth(actor.id - 1), DISTANCE_BETWEEN_ACTORS))
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

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

	}

}


