package com.gnosly.fluentsequence.view.fixedwidth.formatter

import com.gnosly.fluentsequence.view.Coordinates.Pointable
import com.gnosly.fluentsequence.view.fixedwidth.PointableResolverAlgorithms.loopPointableResolverAlgorithm
import com.gnosly.fluentsequence.view.fixedwidth.Fixed2dPoint
import com.gnosly.fluentsequence.view.fixedwidth.painter.FixedWidthPainter
import com.gnosly.fluentsequence.view.model._
import com.gnosly.fluentsequence.view.model.component.{ActivityPoint, ActivityPointForBiSignalOnTheLeft, ActivityPointForBiSignalOnTheRight, ActivityPointLoopOnTheRight}

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

	def format(viewModel: ViewModelComponents): mutable.TreeMap[String, Fixed2dPoint] = {
		val pointables = pointableListFor(viewModel)
		loopPointableResolverAlgorithm.resolve(pointables)
	}

	private def pointableListFor(viewModel: ViewModelComponents): Seq[Pointable] = {
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