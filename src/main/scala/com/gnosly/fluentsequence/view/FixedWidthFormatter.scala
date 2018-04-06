package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.FormatterConstants.ACTOR_DISTANCE
import com.gnosly.fluentsequence.view.fixedwidth.Fixed2DPoint
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
					result.put(topLeftCornerIdForActor(0), Fixed2DPoint(0, 0))
					result.put(topRightCornerIdForActor(0), Fixed2DPoint(15, 0))
				} else {
					result.put(topLeftCornerIdForActor(actorComponent.column),
						result(topRightCornerIdForActor(actorComponent.column - 1)).right(ACTOR_DISTANCE))
					result.put(topRightCornerIdForActor(actorComponent.column),
						result(topLeftCornerIdForActor(actorComponent.column)).right(15))
				}

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
	val ACTOR_DISTANCE = 5
}