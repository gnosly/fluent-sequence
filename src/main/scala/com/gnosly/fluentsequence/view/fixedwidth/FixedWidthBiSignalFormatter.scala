package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.model.BiSignalComponent

class FixedWidthBiSignalFormatter(painter: FixedWidthPainter) {
	def formatOnRight(signal: BiSignalComponent) = {
		//1. prerenderizzazione
		val signalBox = painter.preRender(signal)
		//2. determinazione punto in alto a sx

		//
		//   | |a---------------->| | a= from
		//   | |<---------------a | | a= from
		val activitySide = "right"

		val fromActorId =
			if (signal.leftToRight()) {
				signal.fromActorId
			} else {
				signal.toActorId
			}

		val fromActivityId =
			if (signal.leftToRight()) {
				signal.fromActivityId
			} else {
				signal.toActivityId
			}

		val toActorId =
			if (signal.leftToRight()) {
				signal.toActorId
			} else {
				signal.fromActorId
			}

		val toActivityId =
			if (signal.leftToRight()) {
				signal.toActivityId
			} else {
				signal.fromActivityId
			}

		val activityTopRight = new ReferencePoint(Activity.topRight(fromActorId, fromActivityId))

		val signalYStart = previousIndexPointOrDefaultForBisignal(activityTopRight, toActorId, toActivityId, signal.currentIndex())
		val signalXStart = activityTopRight.right(1).x()
		val signalTopLeft = Fixed2DPoint(signalXStart, signalYStart)

		new SignalPoint(fromActorId, fromActivityId, signal.currentIndex(), signalBox, activitySide, signalTopLeft)
	}


	def formatOnLeft(signal: BiSignalComponent): Pointable = {
		//1. prerenderizzazione
		val signalBox = painter.preRender(signal)
		//2. determinazione punto in alto a sx

		//
		//   | |a---------------->| | a= from
		//   | |<---------------a | | a= from
		val activitySide = "left"

		val fromActorId =
			if (!signal.leftToRight()) {
				signal.fromActorId
			} else {
				signal.toActorId
			}

		val fromActivityId =
			if (!signal.leftToRight()) {
				signal.fromActivityId
			} else {
				signal.toActivityId
			}


		val toActorId =
			if (!signal.leftToRight()) {
				signal.toActorId
			} else {
				signal.fromActorId
			}

		val toActivityId =
			if (!signal.leftToRight()) {
				signal.toActivityId
			} else {
				signal.fromActivityId
			}


		val activityEdge = new ReferencePoint(Activity.topLeft(fromActorId, fromActivityId))

		val signalYStart = previousIndexPointOrDefaultForBisignal(activityEdge, toActorId, toActivityId, signal.currentIndex())
		val signalXStart = activityEdge.x()
		val signalTopLeft = Fixed2DPoint(signalXStart, signalYStart)

		new SignalPoint(fromActorId, fromActivityId, signal.currentIndex(), signalBox, activitySide, signalTopLeft)
	}

	def previousIndexPointOrDefaultForBisignal(activityTop: Point2d, actorId: Int, activityId: Int, signalIndex: Int): Point1d = {
		if (signalIndex == 1) {
			return activityTop.down(1).y()
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

	override def toMatrixConstraints(pointMap: PointMap): Seq[(String, Fixed1DPoint)] = {
		//3. aggiornamento rettangoloni
		val currentRow = ViewMatrix.row(signalIndex)
		val currentColumn = ViewMatrix.column(actorId)

		currentColumn -> Fixed1DPoint(signalBox.width).resolve(pointMap) ::
			currentRow -> fixedPointEnd.y().resolve(pointMap) :: Nil
	}

}
