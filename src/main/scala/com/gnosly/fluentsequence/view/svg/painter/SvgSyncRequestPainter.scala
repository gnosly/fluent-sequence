package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.SyncRequest
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgSyncRequestPainter() extends ComponentPainter[SyncRequest] {
  override def paint(biSignal: SyncRequest, pointMap: ResolvedPoints): SvgCanvas = {

    if (biSignal.fromActorId < biSignal.toActorId) {
      forward(biSignal, pointMap)
    } else {
      backward(biSignal, pointMap)
    }
  }

  private def forward(biSignal: SyncRequest, pointMap: ResolvedPoints) = {
    val signalPoint = pointMap(
      Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex))
    val leftActivityPoint = pointMap(
      Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex))
    val distance = leftActivityPoint.x - signalPoint.x

    new SvgCanvas()
      .drawText(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .drawRightArrow(signalPoint.down(1).left(1), signalPoint.down(1).right(distance))
  }

  private def backward(biSignal: SyncRequest, pointMap: ResolvedPoints) = {
    val signalEndPoint = pointMap(
      Activity.rightPointStart(biSignal.toActorId, biSignal.fromActivityId, biSignal.currentIndex))

    val signalStartPoint = pointMap(
      Activity.leftPointStart(biSignal.fromActorId, biSignal.toActivityId, biSignal.currentIndex))

    val distance = Math.abs(signalStartPoint.x - signalEndPoint.x)

    new SvgCanvas()
      .drawText(signalEndPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
      .drawLeftArrow(signalEndPoint.down(1).left(1), signalEndPoint.down(1).right(distance))
  }
}
