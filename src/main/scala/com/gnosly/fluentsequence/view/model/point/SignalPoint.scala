package com.gnosly.fluentsequence.view.model.point

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.{Activity, Pointable, ViewMatrix, ViewMatrixContenable}
import com.gnosly.fluentsequence.view.fixedwidth._

case class SignalPoint(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box,
											 direction: String, signalTopLeft: Point2d) extends Pointable with ViewMatrixContenable {
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
		if (direction.equals("right")) {
			val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(actorId)).x().resolve(pointMap).x
			val signalStartX = signalTopLeft.resolve(pointMap).x
			point = Fixed1DPoint(signalStartX-actorStartX + signalBox.width)
		}

		currentColumn -> point ::
			currentRow -> fixedPointEnd.y().resolve(pointMap) :: Nil
	}
}
