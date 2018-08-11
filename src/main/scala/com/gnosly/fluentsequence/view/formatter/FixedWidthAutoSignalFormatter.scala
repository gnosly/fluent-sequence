package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.Coordinates.{Activity, Pointable, ViewMatrix}
import com.gnosly.fluentsequence.view.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view._
import com.gnosly.fluentsequence.view.formatter.point.SignalPoint
import com.gnosly.fluentsequence.view.model.component.{AutoSignalComponent, SignalComponent}
import com.gnosly.fluentsequence.view.model.point._

class FixedWidthAutoSignalFormatter(preRenderer: PreRenderer) {

	def format(signal: AutoSignalComponent): Pointable = {
		val signalBox = preRenderer.preRender(signal)
		val activityTopRight = new ReferencePoint(Activity.topRight(signal.fromActorId, signal.fromActivityId))
		//2. determinazione punto in alto a sx
		val signalTopLeft = previousIndexPointOrDefaultForAutoSignal(activityTopRight, signal)

		new SignalPoint(signal.fromActorId, signal.fromActivityId, signal.currentIndex(), signalBox, "right", signalTopLeft)
	}

	def previousIndexPointOrDefaultForAutoSignal(activityTopRight: Point2d, signal: SignalComponent): Point2d = {
		if (isFirstSignal(signal)) {
			return activityTopRight.down(1).right(1)
		} else {
			return Variable2DPoint(activityTopRight.right(1).x(),
				Reference1DPoint(ViewMatrix.row(signal.currentIndex() - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS))
		}
	}

	private def isFirstSignal(signal: SignalComponent) = {
		signal.currentIndex() == 0
	}
}
