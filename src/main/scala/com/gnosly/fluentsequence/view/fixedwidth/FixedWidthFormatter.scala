package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {

	val formatActor = (actor: ActorComponent) => {
		def previousActorDistanceOrDefault(): Point2d = {
			if (actor.id == 0)
				return new Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN)
			else {
				return new ReferencePoint(Actor.topRight(actor.id - 1))
					.right(PointMath.max(Reference1DPoint(s"column_${actor.id - 1}"), Fixed1DPoint(DISTANCE_BETWEEN_ACTORS)))
			}
		}

		//1. prerenderizzazione
		val actorBox = painter.preRender(actor)
		//2. determinazione punto in alto a sx
		val actorTopLeft = previousActorDistanceOrDefault()
		new ActorPoints(actor.id, actorTopLeft, actorBox)
	}

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, VeryFixed2dPoint] = {
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
			formatRule.pointMap.putAll(actorPoints.toPoints(formatRule.pointMap))

			for (activity <- actor.activities) {
				formatActivity(activity, formatRule)
			}
		}
	}

	private def formatActivity(activity: ActivityComponent, formatRule: FormatRule): Any = {
		def previousIndexPointOrDefault(activityTopLeft: Point2d, signal: SignalComponent): Point1d = {
			if (signal.currentIndex() == 1) {
				return activityTopLeft.down(1).y()
			} else {
				if (signal.isInstanceOf[BiSignalComponent]) {
					val biSignal = signal.asInstanceOf[BiSignalComponent]
					val toActivityTopLeft = new ReferencePoint(Activity.topLeft(biSignal.toActorId, biSignal.toActivityId))

					return PointMath.max(Reference1DPoint(s"row_${signal.currentIndex() - 1}") + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS),
						toActivityTopLeft.down(1).y()
					)
				}

				return Reference1DPoint(s"row_${signal.currentIndex() - 1}") + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS)
			}
		}

		val actorBottomMiddle = new ReferencePoint(Actor.bottomMiddle(activity.actorId))
		//1. prerenderizzazione
		val activityBox = painter.preRender(activity)
		//2. determinazione punto in alto a sx

		def activityYStart: Point1d = {
			if (activity.fromIndex > 1) {
				val lastSignalEnd = Reference1DPoint(s"row_${activity.fromIndex - 1}")
				val marginSinceLastActivity = new ReferencePoint(Activity.bottomLeft(activity.actorId, activity.id - 1)).down(1).y()
				return PointMath.max(lastSignalEnd, marginSinceLastActivity)
			} else {
				return actorBottomMiddle.y()
			}
		}

		val activityY = activityYStart

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
			val signalXStart = if (activityPoint.direction.equals("right")) activityTopRight.right(1).x() else activityTopLeft.x()
			val signalTopLeft = Fixed2DPoint(signalXStart, signalYStart)


			//3. aggiornamento rettangoloni
			formatRule.pointMap.put1DPoint(s"column_${actorId}" -> Fixed1DPoint(signalBox.width))
			formatRule.pointMap.put1DPoint(s"row_${signal.currentIndex()}" -> (signalYStart + Fixed1DPoint(signalBox.height)).resolve(formatRule.pointMap))

			formatRule.pointMap.putAll(
				new SignalPoint(actorId, activity.id, signal.currentIndex(), signalBox,
					activityPoint.direction, signalTopLeft).toPoints(formatRule.pointMap)
			)
		}

		val lastPoint = Reference1DPoint(s"row_${activity.toIndex}")

		formatRule.pointMap.putAll(
			new ActivityPoints(actorId, activity.id, activityTopLeft, activityBox.width, lastPoint).toPoints(formatRule.pointMap)
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


	case class ActorPoints(actorId: Int, topLeft: Point2d, actorBox: Box) {
		val actorTopRight = topLeft.right(actorBox.width)
		val actorBottomMiddle = topLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Actor.topLeft(actorId) -> topLeft.resolve(pointMap) ::
				Actor.topRight(actorId) -> actorTopRight.resolve(pointMap) ::
				Actor.bottomMiddle(actorId) -> actorBottomMiddle.resolve(pointMap) :: Nil

		}
	}

	class ActivityPoints(actorId: Int, activityId: Int, val activityTopLeft: Point2d, activityWith: Long, lastPoint: Point1d) {
		val activityTopRight = activityTopLeft.right(activityWith)
		val activityBottomLeft = activityTopLeft.atY(lastPoint)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Activity.topLeft(actorId, activityId) -> activityTopLeft.resolve(pointMap) ::
				Activity.topRight(actorId, activityId) -> activityTopRight.resolve(pointMap) ::
				Activity.bottomLeft(actorId, activityId) -> activityBottomLeft.resolve(pointMap) :: Nil
		}
	}

	class SignalPoint(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box,
										direction: String, signalTopLeft: Point2d) {
		val fixedPointEnd = signalTopLeft.down(signalBox.height)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
				Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
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

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

	}

}




