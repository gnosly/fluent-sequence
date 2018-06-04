package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

class FixedWidthFormatter(painter: FixedWidthPainter) {
	val actorFormatter = new FixedWidthActorFormatter(painter)
	val activityFormatter = new FixedWidthActivityFormatter(painter)
	val autoSignalFormatter = new FixedWidthAutoSignalFormatter(painter)
	val bisignalFormatter = new FixedWidthBiSignalFormatter(painter)
	val formatSignal = (signal: ActivityPoint) => signal match {
		case a: ActivityPointLoopOnTheRight => autoSignalFormatter.format(a.signalComponent)
		case b: ActivityPointForBiSignalOnTheRight => bisignalFormatter.formatOnRight(b.signalComponent)
		case b: ActivityPointForBiSignalOnTheLeft => bisignalFormatter.formatOnLeft(b.signal)
	}

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, VeryFixed2dPoint] = {
		val pointMap = new PointMap()

		while (true) {
			val previousPointMap = pointMap.toMap().toMap
			val pointables = formatIteration(viewModel, pointMap)
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

	private def formatIteration(viewModel: ViewModelComponents, pointMap: PointMap): Seq[Pointable] = {
		val actors = for {
			a <- viewModel._actors.values
		} yield actorFormatter.format(a)

		val activities = for {
			a <- viewModel._actors.values
			b <- a.activities
		} yield activityFormatter.format(b)

		val signals = for {
			a <- viewModel._actors.values
			b <- a.activities
			c <- b.points()
		} yield formatSignal(c)

		return (actors ++ activities ++ signals).toSeq
	}
}