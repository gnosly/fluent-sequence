package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.model.BiSignalComponent

class FixedWidthBiSignalFormatter(painter: FixedWidthPainter) {

	def format(signal: BiSignalComponent, onActivityRightSide: Boolean): Pointable = {
		//1. prerenderizzazione
		val signalBox = painter.preRender(signal)
		//2. determinazione punto in alto a sx

		//
		//   | |a---------------->| | a= from
		//   | |<---------------a | | a= from
		val activitySide = if (onActivityRightSide) "right" else "left"

		val fromActorId = if (onActivityRightSide) {
			if (signal.leftToRight()) {
				signal.fromActorId
			} else {
				signal.toActorId
			}
		} else {
			if (!signal.leftToRight()) {
				signal.fromActorId
			} else {
				signal.toActorId
			}
		}

		val fromActivityId = if (onActivityRightSide) {
			if (signal.leftToRight()) {
				signal.fromActivityId
			} else {
				signal.toActivityId
			}
		} else {
			if (!signal.leftToRight()) {
				signal.fromActivityId
			} else {
				signal.toActivityId
			}
		}

		val toActorId = if (onActivityRightSide) {
			if (signal.leftToRight()) {
				signal.toActorId
			} else {
				signal.fromActorId
			}
		} else {
			if (!signal.leftToRight()) {
				signal.toActorId
			} else {
				signal.fromActorId
			}
		}

		val toActivityId = if (onActivityRightSide) {
			if (signal.leftToRight()) {
				signal.toActivityId
			} else {
				signal.fromActivityId
			}
		} else {
			if (!signal.leftToRight()) {
				signal.toActivityId
			} else {
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


case class SignalPoint(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box,
											 direction: String, signalTopLeft: Point2d) extends Pointable with ViewMatrixContenable {
	private val fixedPointEnd = signalTopLeft.down(signalBox.height)

	def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
		Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
			Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
	}

	override def toMatrixConstraint(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
		//3. aggiornamento rettangoloni
		val currentRow = ViewMatrix.row(signalIndex)
		val currentColumn = ViewMatrix.column(actorId)

		currentColumn -> Fixed1DPoint(signalBox.width).resolve(pointMap).to2d() ::
			currentRow -> fixedPointEnd.y().resolve(pointMap).to2d() :: Nil
	}

}
