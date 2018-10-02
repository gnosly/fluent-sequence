package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.SyncRequest
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgSyncRequestPainter() extends ComponentPainter[SyncRequest] {
  override def paint(biSignal: SyncRequest, pointMap: ResolvedPoints): SvgCanvas = {
    val signalPoint = pointMap(
      Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val leftActivityPoint = pointMap(
      Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val distance = leftActivityPoint.x - signalPoint.x

    new SvgCanvas()
      .drawText(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .drawRightArrow(signalPoint.down(1).left(1), signalPoint.down(1).right(distance))
  }
}
