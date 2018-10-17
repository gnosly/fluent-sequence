package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Point2d
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

case class WidthAndHeightPoint(width: Point2d, height: Point2d) extends Pointable {
  override def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.width() -> width.resolve(pointMap) ::
      ViewMatrix.height() -> height.resolve(pointMap) :: Nil
}
