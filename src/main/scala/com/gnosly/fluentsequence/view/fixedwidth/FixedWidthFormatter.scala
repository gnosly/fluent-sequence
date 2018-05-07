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
		def previousIndexPointOrDefault(activityTopLeft: Fixed2DPoint, signal: SignalComponent): Long = {
			if (signal.currentIndex() == 1) {
				return activityTopLeft.y + 1
			} else {
				return rowHeight(signal.currentIndex() - 1) + DISTANCE_BETWEEN_SIGNALS
			}
		}

		//1. prerenderizzazione
		val activityBox = painter.preRender(activity)
		//2. determinazione punto in alto a sx
		var activityY = 0L
		if (activity.fromIndex > 1) {
			activityY = rowHeight(activity.fromIndex - 1)
		} else {
			activityY = actorBottomMiddle.y
		}

		val activityTopLeft = actorBottomMiddle.left(activityBox.halfWidth).atY(activityY)
		val activityTopRight = activityTopLeft.right(activityBox.width)
		//

		val actorId = activity.actorId

		for (point <- activity.points()) {
			val activityPoint = point._2
			val signal = activityPoint.signalComponent

			//1. prerenderizzazione
			val signalBox = painter.preRender(signal)
			//2. determinazione punto in alto a sx
			val signalYStart = previousIndexPointOrDefault(activityTopLeft, signal)
			val signalXStart = if (activityPoint.direction.equals("right")) activityTopRight.x + 1 else activityTopLeft.x
			//3. aggiornamento rettangoloni
			columnWidth.updateMax(actorId, signalBox.width)
			rowHeight.updateMax(signal.currentIndex(), signalYStart + signalBox.height)

			pointMap.putAll(
				new SignalPoint(actorId, activity.id, signal.currentIndex(), signalBox, activityPoint.direction, signalXStart, signalYStart).toPoints()
			)
		}

		val lastPoint = rowHeight(activity.toIndex)


		pointMap.putAll(
			new ActivityPoints(actorId, activity.id, activityTopLeft, Box(activityBox.width, lastPoint-activityY)).toPoints()
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

		//1. prerenderizzazione
		val actorBox = painter.preRender(actor)
		//2. determinazione punto in alto a sx
		val actorTopLeft = previousActorDistanceOrDefault()
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

	class ActivityPoints(actorId: Int, activityId: Int, activityTopLeft: Fixed2DPoint, activityBox: Box) {
		val activityTopRight = activityTopLeft.right(activityBox.width)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Activity.topLeft(actorId, activityId) -> activityTopLeft ::
				Activity.topRight(actorId, activityId) -> activityTopRight ::
				Activity.bottomLeft(actorId, activityId) -> Fixed2DPoint(activityTopLeft.x, activityTopLeft.down(activityBox.height).y) :: Nil
		}
	}

	class SignalPoint(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box, direction: String, signalXStart: Long, signalYStart: Long) {
		val fixedPointEnd = Fixed2DPoint(signalXStart, signalYStart + signalBox.height)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> Fixed2DPoint(signalXStart, signalYStart) ::
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

		def leftPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "left")

		def pointStart(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_start"

		def rightPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "right")

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

	}

}




