package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncRequest
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

class FixedWidthSyncRequestPainter() extends ComponentPainter[SyncRequest] {

  override def paint(biSignal: SyncRequest, pointMap: ResolvedPoints): FixedWidthCanvas = {

    if (biSignal.isForward) {
      forward(biSignal, pointMap)
    } else {
      backward(biSignal, pointMap)
    }
  }

  private def backward(biSignal: SyncRequest, pointMap: ResolvedPoints) = {
    val signalEndPoint = pointMap(
      Activity.rightPointStart(biSignal.toActorId, biSignal.fromActivityId, biSignal.currentIndex))

    val signalStartPoint = pointMap(
      Activity.leftPointStart(biSignal.fromActorId, biSignal.toActivityId, biSignal.currentIndex))

    val distance = Math.abs(signalStartPoint.x - signalEndPoint.x)

    new FixedWidthCanvas()
      .write(signalEndPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .write(signalEndPoint.down(1), "<" + r("-", distance - 1))
  }

  def r(pattern: String, count: Long): String = (0 until count.toInt).map(_ => pattern).reduce(_ + _)

  private def forward(biSignal: _root_.com.gnosly.fluentsequence.view.model.ViewModels.SyncRequest,
                      pointMap: _root_.com.gnosly.fluentsequence.view.model.point.ResolvedPoints) = {
    val signalStartPoint = pointMap(
      Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val signalEndPoint = pointMap(
      Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))

    val distance = signalEndPoint.x - signalStartPoint.x

    new FixedWidthCanvas()
      .write(signalStartPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .write(signalStartPoint.down(1), r("-", distance - 1) + ">")
  }

}
