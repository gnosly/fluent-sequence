package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Point1d
import com.gnosly.fluentsequence.view.model.point.Point2d
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

case class ActivityPoints(actorId: Int,
                          activityId: Int,
                          activityTopLeft: Point2d,
                          activityWith: Long,
                          lastPoint: Point1d)
    extends Pointable {
  val activityTopRight = activityTopLeft.right(activityWith)
  val activityBottomLeft = activityTopLeft.atY(lastPoint)

  def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] = {
    Activity.topLeft(actorId, activityId) -> activityTopLeft.resolve(pointMap) ::
      Activity.topRight(actorId, activityId) -> activityTopRight.resolve(pointMap) ::
      Activity.bottomLeft(actorId, activityId) -> activityBottomLeft.resolve(pointMap) :: Nil
  }
}
