package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class FixedWidthBiSignalPainter() extends ComponentPainter[BiSignalComponent] {

  override def paint(biSignal: BiSignalComponent, pointMap: Map[String, Fixed2dPoint]): FixedWidthCanvas = {
    val canvas = new FixedWidthCanvas()
    if (biSignal.leftToRight) {
      val signalPoint = pointMap(
        Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
      val leftActivityPoint = pointMap(
        Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
      val distance = leftActivityPoint.x - signalPoint.x

      canvas.write(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      canvas.write(signalPoint.down(1), r("-", distance - 1) + ">")
    } else {
      val signalLeftPoint = pointMap(
        Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
      val signalRightPoint = pointMap(
        Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
      val distance = signalRightPoint.x - signalLeftPoint.x

      canvas.write(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      canvas.write(signalLeftPoint.down(1), "<" + r("-", distance - 1))
    }
    canvas
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
