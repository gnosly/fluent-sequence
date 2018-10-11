package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.loopPointableResolverAlgorithm
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.ViewModel
import com.gnosly.fluentsequence.view.model.component._

class ViewModelFormatter(preRenderer: FixedPreRenderer) {
  private val actorFormatter = new ActorFormatter(preRenderer)
  private val activityFormatter = new ActivityFormatter(preRenderer)
  private val autoSignalFormatter = new AutoSignalFormatter(preRenderer)
  private val bisignalFormatter = new BiSignalFormatter(preRenderer)
  private val columnFormatter = new ColumnFormatter(preRenderer)
  private val rowFormatter = new RowFormatter()
  private val widthAndHeightFormatter = new WidthAndHeightFormatter
  private val alternativeFormatter = new AlternativeFormatter
  private val formatSignal = (signal: PointComponent) =>
    signal match {
      //Fixme: we could separate those formatting
      case b: PointOnTheRight => {
        b.signal match {
          case x: AutoSignalComponent => autoSignalFormatter.format(x)
          case x: BiSignalComponent   => bisignalFormatter.formatOnRight(x)
        }
      }
      case b: PointOnTheLeft => bisignalFormatter.formatOnLeft(b.signal)
  }

  def format(viewModel: ViewModel): ResolvedPoints = {
    val pointables: Seq[Pointable] = pointableListFor(viewModel)
    loopPointableResolverAlgorithm.resolve(pointables)
  }

  private def pointableListFor(viewModel: ViewModel): Seq[Pointable] = {

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
