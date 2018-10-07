package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Point2d
import com.gnosly.fluentsequence.view.model.point.PointMap

case class AlternativePoints(alternativeId: Int, topLeft: Point2d, bottomRight: Point2d) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] =
    Coordinates.Alternative.topLeft(alternativeId) -> topLeft.resolve(pointMap) ::
      Coordinates.Alternative.bottomRight(alternativeId) -> bottomRight.resolve(pointMap) :: Nil
}
