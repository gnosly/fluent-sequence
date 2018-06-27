package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.{ComponentPainter, Fixed2dPoint}

class SvgAutoSignalPainter() extends ComponentPainter[AutoSignalComponent]{
	override def paint(autoSignal: AutoSignalComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
		val canvas = new SvgCanvas()
		val signalPoint = pointMap(Activity.rightPointStart(autoSignal.actorId, autoSignal.activityId, autoSignal.currentIndex()))

		canvas.drawAutoArrow(signalPoint.left(1), signalPoint.down(3).left(1))
		canvas.drawText(signalPoint.down(2).right(3), autoSignal.name)

		canvas
	}
}
