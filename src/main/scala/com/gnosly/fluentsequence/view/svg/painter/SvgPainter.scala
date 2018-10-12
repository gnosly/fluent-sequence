package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.Painter
import com.gnosly.fluentsequence.view.model.ViewModels.ViewModel
import com.gnosly.fluentsequence.view.model.component._

//fixme maybe could be one painter
case class SvgPainter() extends Painter {
  private val actorPainter = new SvgActorPainter
  private val activityPainter = new SvgActivityPainter
  private val syncRequestPainter = new SvgSyncRequestPainter
  private val syncResponsePainter = new SvgSyncResponsePainter
  private val asyncRequestPainter = new SvgAsyncRequestPainter
  private val autoSignalPainter = new SvgAutoSignalPainter

  override def paint(viewModel: ViewModel, pointMap: ResolvedPoints): Canvas = {

    val actorCanvas = viewModel.actorsM.map(a => actorPainter.paint(a, pointMap))

    val activityCanvas = for {
      activity <- viewModel.activities
    } yield activityPainter.paint(activity, pointMap)

    val signalCanvas = for {
      rightPoint <- viewModel.rightPoints
    } yield
      rightPoint.signalComponent match {
        case x: AutoSignalModel => autoSignalPainter.paint(x, pointMap)
        case x: SyncRequest     => syncRequestPainter.paint(x, pointMap)
        case x: SyncResponse    => syncResponsePainter.paint(x, pointMap)
        case x: AsyncRequest    => asyncRequestPainter.paint(x, pointMap)
      }

    val width = pointMap(ViewMatrix.width()).x
    val height = pointMap(ViewMatrix.height()).x

    (actorCanvas ++ activityCanvas ++ signalCanvas)
      .reduce(_.merge(_))
      .withSize(width, height)
  }
}
