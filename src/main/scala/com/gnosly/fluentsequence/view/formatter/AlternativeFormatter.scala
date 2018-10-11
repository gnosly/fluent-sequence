package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.point.AlternativePoints
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint

class AlternativeFormatter {
  def format(alternative: AlternativeComponent): Pointable = {

    AlternativePoints(
      alternative.id,
      new Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0))
        .right(1)
        .atY(new ReferencePoint(ViewMatrix.row(alternative.startIndex)).x),
      new ReferencePoint(ViewMatrix.width())
        .left(2)
        .atY(new ReferencePoint(ViewMatrix.row(alternative.endIndex)).x)
    )
  }
}
