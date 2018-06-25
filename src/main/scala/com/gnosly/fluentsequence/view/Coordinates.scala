package com.gnosly.fluentsequence.view


object Coordinates {

	trait ViewMatrixContenable {
		def toMatrixConstraints(pointMap: PointMap): Seq[(String, Fixed1DPoint)] = Seq()
	}

	trait Pointable extends ViewMatrixContenable {
		def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)]
	}

	object Actor {
		def width(actorId: Int) = s"actor_${actorId}_width"

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
		/**
			* The column is the space between the topLeftCorner of two actors
			*
			*/
		def column(actorId: Int): String = s"column_${actorId}"

		def row(signalIndex: Int): String = s"row_${signalIndex}"
	}

}
