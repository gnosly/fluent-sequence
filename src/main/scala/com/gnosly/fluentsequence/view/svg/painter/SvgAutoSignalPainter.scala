package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgAutoSignalPainter() extends ComponentPainter[AutoSignalModel] {
  override def paint(autoSignal: AutoSignalModel, pointMap: ResolvedPoints): SvgCanvas = {
    val signalPoint = pointMap(
      Activity.rightPointStart(autoSignal.actorId, autoSignal.activityId, autoSignal.currentIndex))

    new SvgCanvas()
      .drawAutoArrow(signalPoint.left(1), signalPoint.down(3).left(1))
      .drawText(signalPoint.down(2).right(3), autoSignal.name)
  }
}
