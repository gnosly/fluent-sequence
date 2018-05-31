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