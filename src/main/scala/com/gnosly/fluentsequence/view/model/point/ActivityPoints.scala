package com.gnosly.fluentsequence.view.model.point

import com.gnosly.fluentsequence.view.Coordinates.{Activity, Pointable}
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2dPoint, Point1d, Point2d, PointMap}

case class ActivityPoints(actorId: Int, activityId: Int, activityTopLeft: Point2d, activityWith: Long, lastPoint: Point1d) extends Pointable {
	val activityTopRight = activityTopLeft.right(activityWith)
	val activityBottomLeft = activityTopLeft.atY(lastPoint)

	def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = {
		Activity.topLeft(actorId, activityId) -> activityTopLeft.resolve(pointMap) ::
			Activity.topRight(actorId, activityId) -> activityTopRight.resolve(pointMap) ::
			Activity.bottomLeft(actorId, activityId) -> activityBottomLeft.resolve(pointMap) :: Nil
	}
}
