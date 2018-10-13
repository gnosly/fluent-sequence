package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncResponse

class FixedWidthSyncResponsePainter() extends ComponentPainter[SyncResponse] {

  override def paint(biSignal: SyncResponse, pointMap: ResolvedPoints): FixedWidthCanvas = {
    val signalLeftPoint = pointMap(
      Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val signalRightPoint = pointMap(
      Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val distance = signalRightPoint.x - signalLeftPoint.x

    new FixedWidthCanvas()
      .write(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .write(signalLeftPoint.down(1), "<" + r("-", distance - 1))
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
