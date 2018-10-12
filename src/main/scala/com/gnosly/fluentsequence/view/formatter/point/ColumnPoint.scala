package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.PointMap

case class ColumnPoint(actorId: Int, columnWidth: Long) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.column(actorId) -> Fixed2dPoint(columnWidth, 0) :: Nil
}
