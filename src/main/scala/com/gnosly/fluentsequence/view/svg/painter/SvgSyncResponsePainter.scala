package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncResponse
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgSyncResponsePainter() extends ComponentPainter[SyncResponse] {

  override def paint(biSignal: SyncResponse, pointMap: ResolvedPoints): SvgCanvas = {

    if (biSignal.isBackward) {
      backward(biSignal, pointMap)
    } else {
      forward(biSignal, pointMap)
    }
  }

  private def backward(biSignal: SyncResponse, pointMap: ResolvedPoints) = {
    val signalLeftPoint = pointMap(
      Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val signalRightPoint = pointMap(
      Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val distance = signalRightPoint.x - signalLeftPoint.x

    new SvgCanvas()
      .drawText(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .drawLeftArrow(signalLeftPoint.down(1).left(1), signalRightPoint.down(1))
  }

  private def forward(biSignal: SyncResponse, pointMap: ResolvedPoints) = {
    val signalPointStart = pointMap(
      Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val signalPointEnd = pointMap(
      Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val distance = signalPointEnd.x - signalPointStart.x

    new SvgCanvas()
      .drawText(signalPointStart.right((distance - biSignal.name.length) / 2), biSignal.name)
      .drawRightArrow(signalPointStart.down(1).left(1), signalPointEnd.down(1))
  }
}
