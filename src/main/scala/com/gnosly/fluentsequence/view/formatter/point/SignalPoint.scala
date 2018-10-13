package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.point._

case class SignalPoint(actorId: Int,
                       activityId: Int,
                       signalIndex: Int,
                       signalBox: Box,
                       direction: String,
                       signalTopLeft: Point2d)
    extends Pointable {

  private val fixedPointEnd = signalTopLeft.down(signalBox.height)

  def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] = {
    Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
      Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
  }

}
