package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, Fixed2DPoint] = {
		val formatRule = FormatRule()

		while (true) {
			val previousPointMap = formatRule.pointMap.toMap().toMap
			formatIteration(viewModel, formatRule)
			if (formatRule.pointMap.toMap().toMap == previousPointMap) {
				return formatRule.pointMap.toMap()
			}
		}

		formatRule.pointMap.toMap()
	}


	private def formatIteration(viewModel: ViewModelComponents, formatRule: FormatRule) = {
		for (actor <- viewModel._actors.values) {
			val actorPoints = formatActor(actor)
			formatRule.pointMap.putAll(actorPoints.toPoints(formatRule.pointMap, formatRule.columnWidth))

			for (activity <- actor.activities) {
				formatActivity(activity, formatRule)
			}
		}
	}

	 def formatActor(actor: ActorComponent) = {
		def previousActorDistanceOrDefault() = {
			if (actor.id == 0)
				Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN)
			else
				ReferencePoint(Actor.topRight(actor.id - 1)).right(PointMath.max(Reference1DPoint(s"column_${actor.id - 1}"), Fixed1DPoint(DISTANCE_BETWEEN_ACTORS)))
		}

		//1. prerenderizzazione
		val actorBox = painter.preRender(actor)
		//2. determinazione punto in alto a sx
		val actorTopLeft = previousActorDistanceOrDefault()
		new ActorPoints(actor.id, actorTopLeft, actorBox)
	}

	private def formatActivity(activity: ActivityComponent, formatRule: FormatRule): Any = {
		def previousIndexPointOrDefault(activityTopLeft: Fixed2DPoint, signal: SignalComponent): Long = {
			if (signal.currentIndex() == 1) {
				return activityTopLeft.y + 1
			} else {
				if (signal.isInstanceOf[BiSignalComponent]) {
					val biSignal = signal.asInstanceOf[BiSignalComponent]
					val toActivityTopLeft = formatRule.pointMap(Activity.topLeft(biSignal.toActorId, biSignal.toActivityId))
					return Math.max(formatRule.rowHeight(signal.currentIndex() - 1) + DISTANCE_BETWEEN_SIGNALS,
						toActivityTopLeft.y + 1)
				}
				return formatRule.rowHeight(signal.currentIndex() - 1) + DISTANCE_BETWEEN_SIGNALS
			}
		}


		val actorBottomMiddle = formatRule.pointMap(Actor.bottomMiddle(activity.actorId))
		//1. prerenderizzazione
		val activityBox = painter.preRender(activity)
		//2. determinazione punto in alto a sx
		var activityY = 0L
		if (activity.fromIndex > 1) {
			val lastSignalEnd = formatRule.rowHeight(activity.fromIndex - 1)
			val marginSinceLastActivity = formatRule.pointMap(Activity.bottomLeft(activity.actorId, activity.id - 1)).y + 1
			activityY = Math.max(lastSignalEnd, marginSinceLastActivity)
		} else {
			activityY = actorBottomMiddle.y
		}

		val activityTopLeft = actorBottomMiddle.left(activityBox.halfWidth()).atY(activityY)
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
			val signalTopLeft = Fixed2DPoint(signalXStart, signalYStart)


			//3. aggiornamento rettangoloni
			formatRule.columnWidth.updateMax(s"column_${actorId}", Fixed1DPoint(signalBox.width))
			formatRule.rowHeight.updateMax(signal.currentIndex(), signalYStart + signalBox.height)

			formatRule.pointMap.putAll(
				new SignalPoint(actorId, activity.id, signal.currentIndex(), signalBox,
					activityPoint.direction, signalTopLeft).toPoints()
			)
		}

		val lastPoint = formatRule.rowHeight(activity.toIndex)

		formatRule.pointMap.putAll(
			new ActivityPoints(actorId, activity.id, activityTopLeft, Box(activityBox.width, lastPoint - activityY)).toPoints()
		)
	}

	private def marginActivityEnd(activity: ActivityComponent, signal: SignalComponent) = {
		if (signal.currentIndex() == activity.toIndex) {
			1
		} else {
			0
		}
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


	case class ActorPoints(actorId: Int, topLeft: Point, actorBox: Box) {
		val actorTopRight = topLeft.right(actorBox.width)
		val actorBottomMiddle = topLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

		def toPoints(pointMap: PointMap, singlePointMap: SinglePointMap): Seq[(String, Fixed2DPoint)] = {
			Actor.topLeft(actorId) -> topLeft.resolve(pointMap, singlePointMap) ::
				Actor.topRight(actorId) -> actorTopRight.resolve(pointMap, singlePointMap) ::
				Actor.bottomMiddle(actorId) -> actorBottomMiddle.resolve(pointMap, singlePointMap) :: Nil

		}
	}

	class ActivityPoints(actorId: Int, activityId: Int, val activityTopLeft: Fixed2DPoint, activityBox: Box) {
		val activityTopRight = activityTopLeft.right(activityBox.width)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Activity.topLeft(actorId, activityId) -> activityTopLeft ::
				Activity.topRight(actorId, activityId) -> activityTopRight ::
				Activity.bottomLeft(actorId, activityId) -> Fixed2DPoint(activityTopLeft.x, activityTopLeft.down(activityBox.height).y) :: Nil
		}
	}

	class SignalPoint(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box,
										direction: String, signalTopLeft: Fixed2DPoint) {
		val fixedPointEnd = signalTopLeft.down(signalBox.height)

		def toPoints(): Seq[(String, Fixed2DPoint)] = {
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft ::
				Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd :: Nil
		}
	}

	case class FormatRule(rowHeight: SingleSize = new SingleSize, columnWidth: SinglePointMap = new SinglePointMap(), pointMap: PointMap = new PointMap)

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

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

	}

}




