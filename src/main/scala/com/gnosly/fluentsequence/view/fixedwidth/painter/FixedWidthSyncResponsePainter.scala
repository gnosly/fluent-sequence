package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncResponse
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

class FixedWidthSyncResponsePainter() extends ComponentPainter[SyncResponse] {

  override def paint(biSignal: SyncResponse, pointMap: ResolvedPoints): FixedWidthCanvas = {

    if (biSignal.fromActorId > biSignal.toActorId) {
      backward(biSignal, pointMap)
    } else {
      forward(biSignal, pointMap)
    }
  }

  private def forward(biSignal: SyncResponse, pointMap: ResolvedPoints) = {
    val signalPointStart = pointMap(
      Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val signalPointEnd = pointMap(
      Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val distance = signalPointEnd.x - signalPointStart.x

    new FixedWidthCanvas()
      .write(signalPointStart.right((distance - biSignal.name.length) / 2), biSignal.name)
      .write(signalPointStart.down(1), r("-", distance - 1) + ">")
  }
  private def backward(biSignal: SyncResponse, pointMap: ResolvedPoints) = {
    val signalPointEnd = pointMap(
      Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val signalPointStart = pointMap(
      Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val distance = signalPointStart.x - signalPointEnd.x

    new FixedWidthCanvas()
      .write(signalPointEnd.right((distance - biSignal.name.length) / 2), biSignal.name)
      .write(signalPointEnd.down(1), "<" + r("-", distance - 1))
  }
  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
