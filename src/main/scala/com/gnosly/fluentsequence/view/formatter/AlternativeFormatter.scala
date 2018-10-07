package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.point.AlternativePoints
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Point2d
import com.gnosly.fluentsequence.view.model.point.PointMap
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModelComponents

class AlternativeFormatter {
  def format(alternative: AlternativeComponent): Pointable = {

    AlternativePoints(alternative.id,
                      new ReferencePoint(ViewMatrix.firstColumn())
                        .atY(new ReferencePoint(ViewMatrix.row(alternative.startIndex)).x))
  }
}
