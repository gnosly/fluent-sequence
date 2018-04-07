package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants._
import com.gnosly.fluentsequence.view.model.ViewModelComponents

import scala.collection.mutable

class FixedWidthFormatter(viewModel: ViewModelComponents, painter: FixedWidthPainter) {


	def format(): Map[String, Fixed2DPoint] = {
		val result = new PointMap

		while (true) {

			val iteration = result.toMap()

			for (actor <- viewModel._actors) {
				val actorComponent = actor._2
				if (actorComponent.column == 0) {
					result.put(topLeftCornerIdForActor(0), Fixed2DPoint(LEFT_MARGIN, TOP_MARGIN))
				} else {
					result.put(topLeftCornerIdForActor(actorComponent.column),
						result(topRightCornerIdForActor(actorComponent.column - 1)).right(DISTANCE_BETWEEN_ACTORS))
				}

				result.put(topRightCornerIdForActor(actorComponent.column),
					result(topLeftCornerIdForActor(actorComponent.column)).right(painter.preRender(actorComponent).x))

			}

			if(result.toMap() == iteration){
				return result.toMap()
			}
		}

		result.toMap()
	}

	private def topLeftCornerIdForActor(column: Int) = {
		s"actor_${column}_top_left"
	}

	private def topRightCornerIdForActor(column: Int) = {
		s"actor_${column}_top_right"
	}


	class PointMap {

		val map: mutable.HashMap[String, Fixed2DPoint] = mutable.HashMap[String, Fixed2DPoint]()

		def put(str: String, point: Fixed2DPoint): Option[Fixed2DPoint] = map.put(str, point)

		def apply(key: String): Fixed2DPoint = {
			if (map.contains(key))
				return map(key)
			return Fixed2DPoint(0, 0)
		}

		def toMap(): Map[String, Fixed2DPoint] = map.toMap
	}

}

object FormatterConstants {
	val DISTANCE_BETWEEN_ACTORS = 10
	val LEFT_MARGIN = 1
	val TOP_MARGIN = 1
}