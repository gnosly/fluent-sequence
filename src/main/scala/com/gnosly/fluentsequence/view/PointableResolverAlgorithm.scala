package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.Coordinates.Pointable

import scala.collection.mutable

object PointableResolverAlgorithms {
	val loopPointableResolverAlgorithm = new LoopPointableResolverAlgorithm()

	trait PointableResolverAlgorithm {
		def resolve(pointables: Seq[Pointable]): mutable.TreeMap[String, Fixed2dPoint]
	}

	class LoopPointableResolverAlgorithm extends PointableResolverAlgorithm {
		override def resolve(pointables: Seq[Pointable]): mutable.TreeMap[String, Fixed2dPoint] = {

			val pointMap = new PointMap()
			while (true) {
				val previousPointMap = pointMap.toMap().toMap

				pointMap.putAll(pointables.flatMap(p => p.toPoints(pointMap)))
				pointMap.put1DPoint(pointables.flatMap(p => p.toMatrixConstraints(pointMap))
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

		private def max(a: Fixed1DPoint, b: Fixed1DPoint): Fixed1DPoint = {
			if (a.x > b.x) a else b
		}

	}

}