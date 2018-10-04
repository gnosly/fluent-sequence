package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates.{Pointable, ViewMatrix}
import com.gnosly.fluentsequence.view.model.point.{Fixed2dPoint, Point2d, PointMap}

case class WidthAndHeightPoint(width: Point2d, height: Point2d) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.width() -> width.resolve(pointMap) ::
    ViewMatrix.height() -> height.resolve(pointMap) :: Nil
}
