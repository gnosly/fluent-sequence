package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates.{Pointable, ViewMatrix}
import com.gnosly.fluentsequence.view.model.point.{Fixed2dPoint, Point1d, PointMap}

case class ColumnPoint(actorId: Int, columnWidth: Point1d) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.column(actorId) -> Fixed2dPoint(columnWidth.resolve(pointMap).x, 0) :: Nil
}
