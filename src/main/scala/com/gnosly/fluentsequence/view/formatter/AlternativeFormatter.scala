package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.point.AlternativePoints
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.AlternativeModel
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint

class AlternativeFormatter {
  def format(alternative: AlternativeModel): Pointable = {

    AlternativePoints(
      alternative.id,
      Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0))
        .right(1)
        .atY(new ReferencePoint(ViewMatrix.row(alternative.startIndex - 1)).x),
      new ReferencePoint(ViewMatrix.width())
        .left(2)
        .atY(new ReferencePoint(ViewMatrix.row(alternative.endIndex)).x)
    )
  }
}
