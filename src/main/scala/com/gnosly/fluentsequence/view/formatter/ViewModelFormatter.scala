package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.loopPointableResolverAlgorithm
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.ViewModels.ViewModel
import com.gnosly.fluentsequence.view.model.ViewModels._

class ViewModelFormatter(preRenderer: FixedPreRenderer) {
  private val actorFormatter = new ActorFormatter(preRenderer)
  private val activityFormatter = new ActivityFormatter(preRenderer)
  private val autoSignalFormatter = new AutoSignalFormatter(preRenderer)
  private val bisignalFormatter = new BiSignalFormatter(preRenderer)
  private val columnFormatter = new ColumnFormatter(preRenderer)
  private val rowFormatter = new RowFormatter()
  private val widthAndHeightFormatter = new WidthAndHeightFormatter
  private val alternativeFormatter = new AlternativeFormatter
  private val formatSignal = (signal: PointModel) =>
    signal match {
      //Fixme: we could separate those formatting
      case b: PointOnTheRight => {
        b.signal match {
          case x: AutoSignalModel => autoSignalFormatter.format(x)
          case x: BiSignalModel   => bisignalFormatter.formatOnRight(x)
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
    } yield
      columnFormatter.format(
        a,
        viewModel.rightPoints.filter(p => {
          p.signalComponent match {
            case x: AsyncRequest if x.fromActorId == a.id => true
            case x: SyncRequest if x.fromActorId == a.id  => true
            case x: AutoSignalModel if x.actorId == a.id  => true
            case x: SyncResponse if x.toActorId == a.id   => true
            case _                                        => false
          }
        })
      )

    val actors = for {
      a <- viewModel.actors
    } yield actorFormatter.format(a)

    val activities = for {
      b <- viewModel.activities
    } yield activityFormatter.format(b)

    val signals = for {
      c <- viewModel.points
    } yield formatSignal(c)

    val rows = for {
      c <- viewModel.points
    } yield rowFormatter.format(c.signalComponent)

    val widthAndHeightPoint = widthAndHeightFormatter.format(viewModel)

    val alternatives = for {
      a <- viewModel.alternatives
    } yield alternativeFormatter.format(a)

    (columns ++ actors ++ activities ++ signals ++ rows ++ List(widthAndHeightPoint) ++ alternatives).toSeq
  }
}
