package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.loopPointableResolverAlgorithm
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model._
import com.gnosly.fluentsequence.view.model.component.{ActivityPoint, ActivityPointForBiSignalOnTheLeft, ActivityPointForBiSignalOnTheRight, ActivityPointLoopOnTheRight}
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class ViewModelFormatter(preRenderer: FixedPreRenderer) {
  val actorFormatter = new ActorFormatter(preRenderer)
  val activityFormatter = new ActivityFormatter(preRenderer)
  val autoSignalFormatter = new AutoSignalFormatter(preRenderer)
  val bisignalFormatter = new BiSignalFormatter(preRenderer)
  val formatSignal = (signal: ActivityPoint) =>
    signal match {
      case a: ActivityPointLoopOnTheRight        => autoSignalFormatter.format(a.signalComponent)
      case b: ActivityPointForBiSignalOnTheRight => bisignalFormatter.formatOnRight(b.signalComponent)
      case b: ActivityPointForBiSignalOnTheLeft  => bisignalFormatter.formatOnLeft(b.signal)
  }

  def format(viewModel: ViewModelComponents): Map[String, Fixed2dPoint] = {
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
      c <- b.points
    } yield formatSignal(c)

    (actors ++ activities ++ signals).toSeq
  }
}
