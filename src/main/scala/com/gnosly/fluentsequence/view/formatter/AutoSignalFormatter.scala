package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.formatter.point.SignalPoint
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.point._

class AutoSignalFormatter(preRenderer: PreRenderer) {

  def format(signal: AutoSignalModel): Pointable = {
    val signalBox = preRenderer.preRender(signal)

    val activityTopRight = new ReferencePoint(Activity.topRight(signal.actorId, signal.activityId))
    //2. determinazione punto in alto a sx
    val signalTopLeft = Variable2DPoint(
      activityTopRight.right(1).x,
      Reference1DPoint(ViewMatrix.row(signal.currentIndex - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS))

    SignalPoint(signal.actorId, signal.activityId, signal.currentIndex, signalBox, "right", signalTopLeft)
  }

}
