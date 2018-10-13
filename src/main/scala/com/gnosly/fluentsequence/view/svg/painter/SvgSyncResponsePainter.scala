package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncResponse
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgSyncResponsePainter() extends ComponentPainter[SyncResponse] {

  override def paint(biSignal: SyncResponse, pointMap: ResolvedPoints): SvgCanvas = {
    val signalLeftPoint = pointMap(
      Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val signalRightPoint = pointMap(
      Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val distance = signalRightPoint.x - signalLeftPoint.x

    new SvgCanvas()
      .drawText(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .drawLeftArrow(signalLeftPoint.down(1).left(1), signalRightPoint.down(1))
  }
}
