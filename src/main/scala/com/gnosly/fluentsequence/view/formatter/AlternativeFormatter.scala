package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.point.AlternativePoints
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.ReferencePoint

class AlternativeFormatter {
  def format(alternative: AlternativeComponent): Pointable = {

    AlternativePoints(
      alternative.id,
      new ReferencePoint(ViewMatrix.firstColumn())
        .atY(new ReferencePoint(ViewMatrix.row(alternative.startIndex)).x),
      new ReferencePoint(ViewMatrix.width())
        .left(1)
        .atY(new ReferencePoint(ViewMatrix.row(alternative.endIndex)).x)
    )
  }
}
