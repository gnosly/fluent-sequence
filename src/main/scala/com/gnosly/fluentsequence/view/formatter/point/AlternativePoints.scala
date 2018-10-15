package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.ALTERNATIVE_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.ALTERNATIVE_MIN_HEIGHT
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Point2d
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

case class AlternativePoints(id: Int, startIndex: Int, endIndex: Int, topLeft: Point2d, bottomRight: Point2d)
    extends Pointable {
  override def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] = {
    List(
      Alternative.topLeft(id) -> topLeft.resolve(pointMap),
      ViewMatrix.row(startIndex) -> pointMap(ViewMatrix.row(startIndex - 1))
        .right(ALTERNATIVE_MIN_HEIGHT + ALTERNATIVE_PADDING), //FIXME
      ViewMatrix.row(endIndex) -> pointMap(ViewMatrix.row(endIndex - 1)).right(ALTERNATIVE_PADDING), //FIXME
      Alternative.bottomRight(id) -> bottomRight.resolve(pointMap)
    )
  }
}
