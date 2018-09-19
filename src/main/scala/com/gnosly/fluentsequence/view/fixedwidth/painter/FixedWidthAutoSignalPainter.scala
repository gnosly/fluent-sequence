package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class FixedWidthAutoSignalPainter() extends ComponentPainter[AutoSignalComponent] {
  override def paint(autoSignal: AutoSignalComponent, pointMap: Map[String, Fixed2dPoint]): FixedWidthCanvas = {
    val signalPoint = pointMap(
      Activity.rightPointStart(autoSignal.actorId, autoSignal.activityId, autoSignal.currentIndex))

    new FixedWidthCanvas()
      .write(signalPoint, "____")
      .write(signalPoint.down(1), "    |")
      .write(signalPoint.down(2), "    | " + autoSignal.name)
      .write(signalPoint.down(3), "<---'")
  }
}
