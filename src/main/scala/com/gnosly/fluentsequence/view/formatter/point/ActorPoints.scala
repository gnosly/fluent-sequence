package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrixContenable
import com.gnosly.fluentsequence.view.model.point._

case class ActorPoints(actorId: Int, topLeft: Point2d, actorBox: Box) extends Pointable with ViewMatrixContenable {
  val actorTopRight = topLeft.right(actorBox.width)
  val actorBottomMiddle = topLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = {
    Actor.topLeft(actorId) -> topLeft.resolve(pointMap) ::
      Actor.topRight(actorId) -> actorTopRight.resolve(pointMap) ::
      Actor.bottomMiddle(actorId) -> actorBottomMiddle.resolve(pointMap) :: Nil

  }

  override def toMatrixConstraints(pointMap: PointMap): Seq[(String, Fixed1DPoint)] = {
    Nil
  }

}
