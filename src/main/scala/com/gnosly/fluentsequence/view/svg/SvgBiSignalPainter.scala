package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.{ComponentPainter, Fixed2dPoint}

class SvgBiSignalPainter() extends ComponentPainter[BiSignalComponent]{
	override def paint(biSignal: BiSignalComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
		val canvas = new SvgCanvas()
		if (biSignal.leftToRight()) {
			val signalPoint = pointMap(Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex()))
			val leftActivityPoint = pointMap(Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex()))
			val distance = leftActivityPoint.x - signalPoint.x

			canvas.drawText(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
			canvas.drawRightArrow(signalPoint.down(1).left(1), leftActivityPoint.down(1))
		} else {
			val signalLeftPoint = pointMap(Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex()))
			val signalRightPoint = pointMap(Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex()))
			val distance = signalRightPoint.x - signalLeftPoint.x

			canvas.drawText(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
			canvas.drawLeftArrow(signalLeftPoint.down(1).left(1), signalRightPoint.down(1))
		}

		canvas
	}
}
