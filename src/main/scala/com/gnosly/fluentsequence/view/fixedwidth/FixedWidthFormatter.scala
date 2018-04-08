package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.{AutoSignalComponent, BiSignalComponent, ViewModelComponents}

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, Fixed2DPoint] = {
		val result = new PointMap

		while (true) {

			val iteration = result.toMap().toMap

			for (actor <- viewModel._actors) {
				val actorComponent = actor._2
				if (actorComponent.id == 0) {
					result.put(topLeftCornerIdForActor(actorComponent.id), Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN))
				} else {
					result.put(topLeftCornerIdForActor(actorComponent.id),
						result(topRightCornerIdForActor(actorComponent.id - 1)).right(DISTANCE_BETWEEN_ACTORS))
				}

				result.put(topRightCornerIdForActor(actorComponent.id),
					result(topLeftCornerIdForActor(actorComponent.id)).right(painter.preRender(actorComponent).x))

				val actorBox = painter.preRender(actorComponent)

				result.put(bottomMiddleCornerIdForActor(actorComponent.id),
					result(topLeftCornerIdForActor(actorComponent.id))
						.right((actorBox.x - 1) / 2)
						.down(actorBox.y)
				)

				for (activity <- actorComponent.activities) {
					if (activity.id == 0) {
						result.put(topLeftCornerIdForActivity(actorComponent.id, activity.id),
							result(bottomMiddleCornerIdForActor(actorComponent.id)).left(painter.preRender(activity)))
						for (point <- activity.rightPoints) {
							point._2 match {
								case autoSignal: AutoSignalComponent => {
									result.put("actor_0_activity_0_right_point_0", Fixed2DPoint(6, 6))
									result.put("actor_0_activity_0_right_point_1", Fixed2DPoint(6, 9))
								}
								case biSignal: BiSignalComponent => {
									result.put("actor_0_activity_0_right_point_0", Fixed2DPoint(6, 6))
								}
							}

						}

						for (point <- activity.leftPoints) {
							point._2 match {
								case biSignal: BiSignalComponent => {
									result.put(pointForActivity(actorComponent.id
										, activity.id, point._1, "left"),
										Fixed2DPoint(19, 6))
								}
							}

						}
					}
				}
			}

			if (result.toMap().toMap == iteration) {
				return result.toMap()
			}
		}

		result.toMap()
	}

	private def topLeftCornerIdForActor(column: Int) = s"actor_${column}_top_left"

	private def topRightCornerIdForActor(column: Int) = s"actor_${column}_top_right"

	private def bottomMiddleCornerIdForActor(column: Int): String = s"actor_${column}_bottom_middle"

	private def topLeftCornerIdForActivity(column: Int, id: Int): String = s"actor_${column}_activity_${id}_top_left"

	private def pointForActivity(actor: Int, activity: Int, point: Int, direction: String): String = s"actor_${actor}_activity_${activity}_${direction}_point_${point}"


	class PointMap {

		private val defaultOrdering = new Ordering[String]() {
			override def compare(a: String, b: String): Int = a.compareTo(b)
		}

		val map: mutable.TreeMap[String, Fixed2DPoint] = mutable.TreeMap[String, Fixed2DPoint]()(defaultOrdering)

		def put(str: String, point: Fixed2DPoint): Option[Fixed2DPoint] = map.put(str, point)

		def apply(key: String): Fixed2DPoint = {
			if (map.contains(key))
				return map(key)
			return Fixed2DPoint(0, 0)
		}

		def toMap(): mutable.TreeMap[String, Fixed2DPoint] = map
	}

}

object FormatterConstants {
	val DISTANCE_BETWEEN_ACTORS = 10
	val LEFT_MARGIN = 1
	val TOP_MARGIN = 1
}