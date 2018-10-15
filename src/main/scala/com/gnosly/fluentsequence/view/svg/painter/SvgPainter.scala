package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.Painter
import com.gnosly.fluentsequence.view.model.ViewModels._
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

//fixme maybe could be one painter
case class SvgPainter() extends Painter {
  private val actorPainter = new SvgActorPainter
  private val activityPainter = new SvgActivityPainter
  private val syncRequestPainter = new SvgSyncRequestPainter
  private val syncResponsePainter = new SvgSyncResponsePainter
  private val asyncRequestPainter = new SvgAsyncRequestPainter
  private val autoSignalPainter = new SvgAutoSignalPainter
  private val alternativePainter = new SvgAlternativePainter()

  override def paint(viewModel: ViewModel, resolvedPoints: ResolvedPoints): Canvas = {

    val actorCanvas = viewModel.actors.map(a => actorPainter.paint(a, resolvedPoints))

    val activityCanvas = for {
      activity <- viewModel.activities
    } yield activityPainter.paint(activity, resolvedPoints)

    val signalCanvas = for {
      rightPoint <- viewModel.rightPoints
    } yield
      rightPoint.signalComponent match {
        case x: AutoSignalModel => autoSignalPainter.paint(x, resolvedPoints)
        case x: SyncRequest     => syncRequestPainter.paint(x, resolvedPoints)
        case x: SyncResponse    => syncResponsePainter.paint(x, resolvedPoints)
        case x: AsyncRequest    => asyncRequestPainter.paint(x, resolvedPoints)
      }

    val alternativeCanvas = for {
      a <- viewModel.alternatives
    } yield alternativePainter.paint(a, resolvedPoints)

    val width = resolvedPoints(ViewMatrix.width()).x
    val height = resolvedPoints(ViewMatrix.height()).x

    (actorCanvas ++ activityCanvas ++ signalCanvas ++ alternativeCanvas)
      .reduce(_.merge(_))
      .withSize(width, height)
  }
}
