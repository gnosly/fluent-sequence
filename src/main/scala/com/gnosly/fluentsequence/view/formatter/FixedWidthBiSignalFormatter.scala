package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.formatter.point.SignalPoint
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.model.point._

class FixedWidthBiSignalFormatter(preRenderer: PreRenderer) {
	//   | |a---------------->| | a= from
	//   | |<---------------a | | a= from
	def formatOnRight(signal: BiSignalComponent) = {
		//1. prerenderizzazione
		val signalBox = preRenderer.preRender(signal)
		//2. determinazione punto in alto a sx
		val activitySide = "right"
		val leftToRight = signal.leftToRight

		val fromActorId =
			if (leftToRight) {
				signal.fromActorId
			} else {
				signal.toActorId
			}

		val fromActivityId =
			if (leftToRight) {
				signal.fromActivityId
			} else {
				signal.toActivityId
			}

		val toActorId =
			if (leftToRight) {
				signal.toActorId
			} else {
				signal.fromActorId
			}

		val toActivityId =
			if (leftToRight) {
				signal.toActivityId
			} else {
				signal.fromActivityId
			}

		val activityTopRight = new ReferencePoint(Activity.topRight(fromActorId, fromActivityId))

		val signalYStart = previousIndexPointOrDefaultForBisignal(activityTopRight, toActorId, toActivityId, signal.currentIndex())
		val signalXStart = activityTopRight.right(1).x()
		val signalTopLeft = Variable2DPoint(signalXStart, signalYStart)

		new SignalPoint(fromActorId, fromActivityId, signal.currentIndex(), signalBox, activitySide, signalTopLeft)
	}

	def previousIndexPointOrDefaultForBisignal(activityTop: Point2d, actorId: Int, activityId: Int, signalIndex: Int): Point1d = {
		if (isFirstSignal(signalIndex)) {
			return activityTop.down(1).y()
		} else {

			val toActivityTopLeft = new ReferencePoint(Activity.topLeft(actorId, activityId))

			return PointMath.max(Reference1DPoint(ViewMatrix.row(signalIndex - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS),
				toActivityTopLeft.down(1).y()
			)
		}
	}

	private def isFirstSignal(signalIndex: Int) = {
		signalIndex == 0
	}

	def formatOnLeft(signal: BiSignalComponent): Pointable = {
		//1. prerenderizzazione
		val signalBox = preRenderer.preRender(signal)
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
		val signalTopLeft = Variable2DPoint(signalXStart, signalYStart)

		new SignalPoint(fromActorId, fromActivityId, signal.currentIndex(), signalBox, activitySide, signalTopLeft)
	}
}