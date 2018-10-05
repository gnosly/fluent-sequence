package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.point.WidthAndHeightPoint
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModelComponents
import com.gnosly.fluentsequence.view.model.point._

class WidthAndHeightFormatter {
  def format(model: ViewModelComponents): Pointable = {
    val width = new ReferencePoint(Actor.topLeft(model.lastActorId))
      .right(new ReferencePoint(ViewMatrix.column(model.lastActorId)).x)
      .atY(0)
      .right(FormatterConstants.RIGHT_MARGIN)

    val height = new ReferencePoint(ViewMatrix.row(model.lastSignalIndex)).right(FormatterConstants.BOTTOM_MARGIN)

    WidthAndHeightPoint(width, height)
  }
}
