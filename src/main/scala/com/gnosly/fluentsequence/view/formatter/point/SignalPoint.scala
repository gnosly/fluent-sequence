package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point._

case class SignalPoint(actorId: Int,
                       activityId: Int,
                       signalIndex: Int,
                       signalBox: Box,
                       direction: String,
                       signalTopLeft: Point2d)
    extends Pointable {

  private val fixedPointEnd = signalTopLeft.down(signalBox.height)

  def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = {
    Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
      Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
  }

  override def toMatrixConstraints(pointMap: PointMap): Seq[(String, Fixed1DPoint)] = {
    //3. aggiornamento rettangoloni
    val currentRow = ViewMatrix.row(signalIndex)
    val currentColumn = ViewMatrix.column(actorId)

    var point = Fixed1DPoint(0)
    if (direction == "right") {
      val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(actorId)).x.resolve(pointMap).x
      val signalStartX = signalTopLeft.resolve(pointMap).x
      point = Fixed1DPoint(signalStartX - actorStartX + signalBox.width)
    }

    currentColumn -> point ::
      currentRow -> fixedPointEnd.y.resolve(pointMap) ::
      Nil
  }
}
