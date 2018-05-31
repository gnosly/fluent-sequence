package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.fixedwidth.PointMath.max
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {
	val actorFormatter = new FixedWidthActorFormatter(painter)
	val activityFormatter = new FixedWidthActivityFormatter(painter)

	val autoSignalFormatter = (signal: AutoSignalComponent) => {
		val signalBox = painter.preRender(signal)
		val activityTopLeft = new ReferencePoint(Activity.topLeft(signal.fromActorId, signal.fromActivityId))
		val activityTopRight = new ReferencePoint(Activity.topRight(signal.fromActorId, signal.fromActivityId))
		//2. determinazione punto in alto a sx
		val signalYStart = previousIndexPointOrDefaultForAutoSignal(activityTopLeft, signal)
		//TODO
		val signalTopLeft = Fixed2DPoint(activityTopRight.right(1).x(), signalYStart)

		new SignalPoint(signal.fromActorId, signal.fromActivityId, signal.currentIndex(), signalBox, "right", signalTopLeft)
	}
	val bisignalFormatter = (signal: BiSignalComponent, onActivityRightSide: Boolean) => {
		//1. prerenderizzazione
		val signalBox = painter.preRender(signal)
		//2. determinazione punto in alto a sx

		//
		//   | |a---------------->| | a= from
		//   | |<---------------a | | a= from
		val activitySide = if (onActivityRightSide) "right" else "left"

		val fromActorId = if (onActivityRightSide) {
			if(signal.leftToRight()){
				signal.fromActorId
			}else{
				signal.toActorId
			}
		} else {
			if(!signal.leftToRight()){
				signal.fromActorId
			}else{
				signal.toActorId
			}
		}

		val fromActivityId = if (onActivityRightSide) {
			if(signal.leftToRight()){
				signal.fromActivityId
			}else{
				signal.toActivityId
			}
		} else {
			if(!signal.leftToRight()){
				signal.fromActivityId
			}else{
				signal.toActivityId
			}
		}

		val toActorId = if (onActivityRightSide) {
			if(signal.leftToRight()){
				signal.toActorId
			}else{
				signal.fromActorId
			}
		} else {
			if(!signal.leftToRight()){
				signal.toActorId
			}else{
				signal.fromActorId
			}
		}

		val toActivityId = if (onActivityRightSide) {
			if(signal.leftToRight()){
				signal.toActivityId
			}else{
				signal.fromActivityId
			}
		} else {
			if(!signal.leftToRight()){
				signal.toActivityId
			}else{
				signal.fromActivityId
			}
		}


		val activityTopLeft = new ReferencePoint(Activity.topLeft(fromActorId, fromActivityId))
		val activityTopRight = new ReferencePoint(Activity.topRight(fromActorId, fromActivityId))

		val signalYStart = previousIndexPointOrDefaultForBisignal(activityTopLeft, toActorId, toActivityId, signal.currentIndex())
		val signalXStart = if (onActivityRightSide) activityTopRight.right(1).x() else activityTopLeft.x()
		val signalTopLeft = Fixed2DPoint(signalXStart, signalYStart)


		new SignalPoint(fromActorId, fromActivityId, signal.currentIndex(), signalBox, activitySide, signalTopLeft)
	}

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, VeryFixed2dPoint] = {
		val pointMap = new PointMap()

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
			val actorPoints = actorFormatter.formatActor(actor)
			pointMap.putAll(actorPoints.toPoints(pointMap))

			for (activity <- actor.activities) {
				val activityPoints = activityFormatter.formatActivity(activity)
				pointMap.putAll(activityPoints.toPoints(pointMap))

				for (point <- activity.points()) {

					val signalPoints = point._2.signalComponent match {
						case a: AutoSignalComponent => autoSignalFormatter(a)
						case b: BiSignalComponent => bisignalFormatter(b, point._2.outgoing)
					}
					//3. aggiornamento rettangoloni
					val currentRow = ViewMatrix.row(signalPoints.signalIndex)
					val currentColumn = ViewMatrix.column(signalPoints.actorId)
					pointMap.put1DPoint(currentColumn -> max(Reference1DPoint(currentColumn), Fixed1DPoint(signalPoints.signalBox.width)).resolve(pointMap))
					pointMap.put1DPoint(currentRow -> max(Reference1DPoint(currentRow), (signalPoints.fixedPointEnd.y())).resolve(pointMap))

					pointMap.putAll(signalPoints.toPoints(pointMap))
				}
			}
		}
	}

	def previousIndexPointOrDefaultForAutoSignal(activityTopLeft: Point2d, signal: SignalComponent): Point1d = {
		if (signal.currentIndex() == 1) {
			return activityTopLeft.down(1).y()
		} else {
			return Reference1DPoint(ViewMatrix.row(signal.currentIndex() - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS)
		}
	}

	def previousIndexPointOrDefaultForBisignal(activityTopLeft: Point2d, actorId:Int, activityId:Int, signalIndex:Int): Point1d = {
		if (signalIndex == 1) {
			return activityTopLeft.down(1).y()
		} else {

			val toActivityTopLeft = new ReferencePoint(Activity.topLeft(actorId, activityId))

			return PointMath.max(Reference1DPoint(ViewMatrix.row(signalIndex - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS),
				toActivityTopLeft.down(1).y()
			)
		}
	}

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

	case class ActivityPoints(actorId: Int, activityId: Int, activityTopLeft: Point2d, activityWith: Long, lastPoint: Point1d) {
		val activityTopRight = activityTopLeft.right(activityWith)
		val activityBottomLeft = activityTopLeft.atY(lastPoint)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Activity.topLeft(actorId, activityId) -> activityTopLeft.resolve(pointMap) ::
				Activity.topRight(actorId, activityId) -> activityTopRight.resolve(pointMap) ::
				Activity.bottomLeft(actorId, activityId) -> activityBottomLeft.resolve(pointMap) :: Nil
		}
	}

	class SignalPoint(val actorId: Int, val activityId: Int, val signalIndex: Int, val signalBox: Box,
										direction: String, signalTopLeft: Point2d) {
		val fixedPointEnd = signalTopLeft.down(signalBox.height)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
				Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
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

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")
	}

	object ViewMatrix {
		def column(actorId: Int): String = s"column_${actorId}"

		def row(signalIndex: Int): String = s"row_${signalIndex}"
	}

}




