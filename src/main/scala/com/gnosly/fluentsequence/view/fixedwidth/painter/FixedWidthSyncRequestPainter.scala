package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncRequest

class FixedWidthSyncRequestPainter() extends ComponentPainter[SyncRequest] {

  override def paint(biSignal: SyncRequest, pointMap: ResolvedPoints): FixedWidthCanvas = {
    val signalPoint = pointMap(
      Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val leftActivityPoint = pointMap(
      Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val distance = leftActivityPoint.x - signalPoint.x

    new FixedWidthCanvas()
      .write(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .write(signalPoint.down(1), r("-", distance - 1) + ">")
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
