package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {
	val actorFormatter = new FixedWidthActorFormatter(painter)
	val activityFormatter = new FixedWidthActivityFormatter(painter)
	val autoSignalFormatter = new FixedWidthAutoSignalFormatter(painter)
	val bisignalFormatter = new FixedWidthBiSignalFormatter(painter)

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, VeryFixed2dPoint] = {
		val pointMap = new PointMap()

		while (true) {
			val previousPointMap = pointMap.toMap().toMap
			val pointables = formatIteration(viewModel, pointMap)
			pointMap.putAll(pointables.flatMap(p => p.toPoints(pointMap)))
			pointMap.putAll(pointables.flatMap(p => p.toMatrixConstraint(pointMap))
				.groupBy[String](_._1)
				.mapValues(x => x.map(_._2))
				.mapValues(_.reduce((a, b) => max(a, b)))
				.toSeq)

			if (pointMap.toMap().toMap == previousPointMap) {
				return pointMap.toMap()
			}
		}

		pointMap.toMap()
	}


	def max(a: VeryFixed2dPoint, b: VeryFixed2dPoint): VeryFixed2dPoint = {
		if (a.x > b.x) a else b
	}

	private def formatIteration(viewModel: ViewModelComponents, pointMap: PointMap): Seq[Pointable] = {
		val pointable = mutable.ArrayBuffer[Pointable]()

		for (actor <- viewModel._actors.values) {
			val actorPoints = actorFormatter.formatActor(actor)
			pointable += actorPoints

			for (activity <- actor.activities) {
				val activityPoints = activityFormatter.format(activity)
				pointable += activityPoints

				for (point <- activity.points()) {

					val signalPoints = point._2.signalComponent match {
						case a: AutoSignalComponent => autoSignalFormatter.format(a)
						case b: BiSignalComponent => bisignalFormatter.format(b, point._2.outgoing)
					}

					pointable += signalPoints
				}
			}
		}

		return pointable
	}
}

object Coordinates {

	trait ViewMatrixContenable {
		def toMatrixConstraint(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = Seq()
	}

	trait Pointable extends ViewMatrixContenable {
		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)]
	}

	case class SignalPoint(actorId: Int, activityId: Int, signalIndex: Int, signalBox: Box,
												 direction: String, signalTopLeft: Point2d) extends Pointable with ViewMatrixContenable {
		private val fixedPointEnd = signalTopLeft.down(signalBox.height)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
				Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
		}

		override def toMatrixConstraint(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			//3. aggiornamento rettangoloni
			val currentRow = ViewMatrix.row(signalIndex)
			val currentColumn = ViewMatrix.column(actorId)

			currentColumn -> Fixed1DPoint(signalBox.width).resolve(pointMap).to2d() ::
				currentRow -> fixedPointEnd.y().resolve(pointMap).to2d() :: Nil
		}

	}

	object Actor {
		def topLeft(actorId: Int) = s"actor_${actorId}_top_left"

		def topRight(actorId: Int) = s"actor_${actorId}_top_right"

		def bottomMiddle(actorId: Int): String = s"actor_${actorId}_bottom_middle"
	}

	object Activity {

		def topLeft(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_top_left"

		def topRight(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_top_right"

		def bottomLeft(actorId: Int, activityId: Int): String = s"actor_${actorId}_activity_${activityId}_bottom_left"

		def rightPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "right")

		def pointStart(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_start"

		def leftPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "left")

		def rightPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "right")

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"
	}

	object ViewMatrix {
		def column(actorId: Int): String = s"column_${actorId}"

		def row(signalIndex: Int): String = s"row_${signalIndex}"
	}

}




