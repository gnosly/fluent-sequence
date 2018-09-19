package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgAutoSignalPainter() extends ComponentPainter[AutoSignalComponent] {
  override def paint(autoSignal: AutoSignalComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
    val canvas = new SvgCanvas()
    val signalPoint = pointMap(
      Activity.rightPointStart(autoSignal.actorId, autoSignal.activityId, autoSignal.currentIndex))

    canvas.drawAutoArrow(signalPoint.left(1), signalPoint.down(3).left(1))
    canvas.drawText(signalPoint.down(2).right(3), autoSignal.name)

    canvas
  }
}
