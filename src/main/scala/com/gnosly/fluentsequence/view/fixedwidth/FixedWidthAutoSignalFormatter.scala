package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.{Activity, SignalPoint, ViewMatrix}
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.model.{AutoSignalComponent, SignalComponent}

class FixedWidthAutoSignalFormatter(painter: FixedWidthPainter) {

	def format(signal: AutoSignalComponent): SignalPoint = {
		val signalBox = painter.preRender(signal)
		val activityTopLeft = new ReferencePoint(Activity.topLeft(signal.fromActorId, signal.fromActivityId))
		val activityTopRight = new ReferencePoint(Activity.topRight(signal.fromActorId, signal.fromActivityId))
		//2. determinazione punto in alto a sx
		val signalYStart = previousIndexPointOrDefaultForAutoSignal(activityTopLeft, signal)
		//TODO
		val signalTopLeft = Fixed2DPoint(activityTopRight.right(1).x(), signalYStart)

		new SignalPoint(signal.fromActorId, signal.fromActivityId, signal.currentIndex(), signalBox, "right", signalTopLeft)
	}

	def previousIndexPointOrDefaultForAutoSignal(activityTopLeft: Point2d, signal: SignalComponent): Point1d = {
		if (signal.currentIndex() == 1) {
			return activityTopLeft.down(1).y()
		} else {
			return Reference1DPoint(ViewMatrix.row(signal.currentIndex() - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS)
		}
	}

}
