package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.PointMath.max
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
			formatIteration(viewModel, pointMap)
			if (pointMap.toMap().toMap == previousPointMap) {
				return pointMap.toMap()
			}
		}

		pointMap.toMap()
	}

	private def formatIteration(viewModel: ViewModelComponents, pointMap: PointMap) = {
		for (actor <- viewModel._actors.values) {
			val actorPoints = actorFormatter.formatActor(actor)

			for (activity <- actor.activities) {
				val activityPoints = activityFormatter.format(activity)

				for (point <- activity.points()) {

					val signalPoints = point._2.signalComponent match {
						case a: AutoSignalComponent => autoSignalFormatter.format(a)
						case b: BiSignalComponent => bisignalFormatter.format(b, point._2.outgoing)
					}

					pointMap.putAll(signalPoints.toPoints(pointMap))
				}

				pointMap.putAll(activityPoints.toPoints(pointMap))
			}


			pointMap.putAll(actorPoints.toPoints(pointMap))
		}
	}
}

object Coordinates {

	case class ActorPoints(actorId: Int, topLeft: Point2d, actorBox: Box) {
		val actorTopRight = topLeft.right(actorBox.width)
		val actorBottomMiddle = topLeft.right((actorBox.width - 1) / 2).down(actorBox.height)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Actor.topLeft(actorId) -> topLeft.resolve(pointMap) ::
				Actor.topRight(actorId) -> actorTopRight.resolve(pointMap) ::
				Actor.bottomMiddle(actorId) -> actorBottomMiddle.resolve(pointMap) :: Nil

		}
	}

	case class ActivityPoints(actorId: Int, activityId: Int, activityTopLeft: Point2d, activityWith: Long, lastPoint: Point1d) {
		val activityTopRight = activityTopLeft.right(activityWith)
		val activityBottomLeft = activityTopLeft.atY(lastPoint)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			Activity.topLeft(actorId, activityId) -> activityTopLeft.resolve(pointMap) ::
				Activity.topRight(actorId, activityId) -> activityTopRight.resolve(pointMap) ::
				Activity.bottomLeft(actorId, activityId) -> activityBottomLeft.resolve(pointMap) :: Nil
		}
	}

	case class SignalPoint(val actorId: Int, val activityId: Int, val signalIndex: Int, val signalBox: Box,
										direction: String, signalTopLeft: Point2d) {
		val fixedPointEnd = signalTopLeft.down(signalBox.height)

		def toPoints(pointMap: PointMap): Seq[(String, VeryFixed2dPoint)] = {
			//3. aggiornamento rettangoloni
			val currentRow = ViewMatrix.row(signalIndex)
			val currentColumn = ViewMatrix.column(actorId)

			currentColumn -> max(Reference1DPoint(currentColumn), Fixed1DPoint(signalBox.width)).resolve(pointMap).to2d() ::
			currentRow -> max(Reference1DPoint(currentRow), fixedPointEnd.y()).resolve(pointMap).to2d() ::
			Activity.pointStart(actorId, activityId, signalIndex, direction) -> signalTopLeft.resolve(pointMap) ::
				Activity.pointEnd(actorId, activityId, signalIndex, direction) -> fixedPointEnd.resolve(pointMap) :: Nil
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

		def leftPointStart(actorId: Int, activityId: Int, pointId: Int): String = pointStart(actorId, activityId, pointId, "left")

		def pointStart(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_start"

		def rightPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "right")

		def pointEnd(actorId: Int, activityId: Int, pointId: Int, direction: String) = s"actor_${actorId}_activity_${activityId}_${direction}_point_${pointId}_end"

		def leftPointEnd(actorId: Int, activityId: Int, pointId: Int): String = pointEnd(actorId, activityId, pointId, "left")
	}

	object ViewMatrix {
		def column(actorId: Int): String = s"column_${actorId}"

		def row(signalIndex: Int): String = s"row_${signalIndex}"
	}

}




