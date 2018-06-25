package com.gnosly.fluentsequence.view.model.point

import com.gnosly.fluentsequence.view._
import com.gnosly.fluentsequence.view.Coordinates.{Actor, Pointable, ViewMatrix, ViewMatrixContenable}
import com.gnosly.fluentsequence.view.fixedwidth._
import com.gnosly.fluentsequence.view.model.Box

case class ActorPoints(actorId: Int, topLeft: Point2d, actorBox: Box) extends Pointable with ViewMatrixContenable {
	val actorTopRight = topLeft.right(actorBox.width)
	val actorBottomMiddle = topLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

	override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = {
		Actor.topLeft(actorId) -> topLeft.resolve(pointMap) ::
			Actor.topRight(actorId) -> actorTopRight.resolve(pointMap) ::
			Actor.bottomMiddle(actorId) -> actorBottomMiddle.resolve(pointMap) :: Nil

	}

	override def toMatrixConstraints(pointMap: PointMap): Seq[(String, Fixed1DPoint)] = {
		ViewMatrix.column(actorId) -> columnWidth(pointMap) :: Nil
	}

	private def columnWidth(pointMap: PointMap): Fixed1DPoint = {
		val midWidth = actorBox.width / 2

		val nextActorTopLeftCorner = new ReferencePoint(Coordinates.Actor.topLeft(actorId + 1)).resolve(pointMap)
		if (nextActorTopLeftCorner == Fixed2dPoint(0L, 0L)) {
			return Fixed1DPoint(midWidth)
		}
		return Fixed1DPoint(actorBox.width + FormatterConstants.DISTANCE_BETWEEN_ACTORS)
	}
}
