package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Point2d
import com.gnosly.fluentsequence.view.model.point.PointMap
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

case class AlternativePoints(alternativeId: Int, topLeft: Point2d, bottomRight: Point2d) extends Pointable {
  override def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] =
    Alternative.topLeft(alternativeId) -> topLeft.resolve(pointMap) ::
      Alternative.bottomRight(alternativeId) -> bottomRight.resolve(pointMap) :: Nil
}
