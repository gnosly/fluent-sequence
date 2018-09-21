package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.AsyncRequest
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class FixedWidthAsyncRequestPainter() extends ComponentPainter[AsyncRequest] {

  override def paint(biSignal: AsyncRequest, pointMap: Map[String, Fixed2dPoint]): FixedWidthCanvas = {
    if (biSignal.leftToRight) {
      val signalPoint = pointMap(
        Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
      val leftActivityPoint = pointMap(
        Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
      val distance = leftActivityPoint.x - signalPoint.x

      new FixedWidthCanvas()
        .write(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
        .write(signalPoint.down(1), r("-", distance - 1) + ">")
    } else {
      val signalLeftPoint = pointMap(
        Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
      val signalRightPoint = pointMap(
        Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
      val distance = signalRightPoint.x - signalLeftPoint.x

      new FixedWidthCanvas()
        .write(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
        .write(signalLeftPoint.down(1), "<" + r("-", distance - 1))
    }
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
