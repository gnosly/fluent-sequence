package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.loopPointableResolverAlgorithm
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.ViewModelComponents
import com.gnosly.fluentsequence.view.model.component.ActivityPoint
import com.gnosly.fluentsequence.view.model.component.ActivityPointForBiSignalOnTheLeft
import com.gnosly.fluentsequence.view.model.component.ActivityPointForBiSignalOnTheRight
import com.gnosly.fluentsequence.view.model.component.ActivityPointLoopOnTheRight

class ViewModelFormatter(preRenderer: FixedPreRenderer) {
  private val actorFormatter = new ActorFormatter(preRenderer)
  private val activityFormatter = new ActivityFormatter(preRenderer)
  private val autoSignalFormatter = new AutoSignalFormatter(preRenderer)
  private val bisignalFormatter = new BiSignalFormatter(preRenderer)
  private val columnFormatter = new ColumnFormatter(preRenderer)
  private val rowFormatter = new RowFormatter()
  private val widthAndHeightFormatter = new WidthAndHeightFormatter
  private val alternativeFormatter = new AlternativeFormatter
  private val formatSignal = (signal: ActivityPoint) =>
    signal match {
      case a: ActivityPointLoopOnTheRight => autoSignalFormatter.format(a.signalComponent)
      //Fixme: we could separate those formatting
      case b: ActivityPointForBiSignalOnTheRight => bisignalFormatter.formatOnRight(b.signalComponent)
      case b: ActivityPointForBiSignalOnTheLeft  => bisignalFormatter.formatOnLeft(b.signal)
  }

  def format(viewModel: ViewModelComponents): ResolvedPoints = {
    val pointables: Seq[Pointable] = pointableListFor(viewModel)
    loopPointableResolverAlgorithm.resolve(pointables)
  }

  private def pointableListFor(viewModel: ViewModelComponents): Seq[Pointable] = {

    val columns = for {
      a <- viewModel.actors
    } yield columnFormatter.format(a)

    val actors = for {
      a <- viewModel.actors
    } yield actorFormatter.format(a)

    val activities = for {
      a <- viewModel.actors
      b <- a.activities
    } yield activityFormatter.format(b)

    val signals = for {
      a <- viewModel.actors
      b <- a.activities
      c <- b.points
    } yield formatSignal(c) //FIXME for actors and activities I format the component itself. Here the point

    val rows = for {
      a <- viewModel.actors
      b <- a.activities
      c <- b.points
    } yield rowFormatter.format(c.signalComponent)

    val widthAndHeightPoint = widthAndHeightFormatter.format(viewModel)

    val alternatives = for {
      a <- viewModel.alternatives
    } yield alternativeFormatter.format(a)

    (columns ++ actors ++ activities ++ signals ++ rows ++ List(widthAndHeightPoint) ++ alternatives).toSeq
  }
}
