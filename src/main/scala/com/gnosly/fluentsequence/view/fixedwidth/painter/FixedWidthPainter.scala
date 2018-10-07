package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model._
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class FixedWidthPainter extends Painter {
  private val actorPainter = new FixedWidthActorPainter
  private val asyncRequestPainter = new FixedWidthAsyncRequestPainter
  private val syncRequestPainter = new FixedWidthSyncRequestPainter
  private val syncResponsePainter = new FixedWidthSyncResponsePainter
  private val autoSignalPainter = new FixedWidthAutoSignalPainter
  private val activityPainter = new FixedWidthActivityPainter

  override def paint(viewModel: ViewModelComponents, pointMap: ResolvedPoints): Canvas = {
    val titleCanvas = paintTitle(viewModel, pointMap)

    val actorCanvas = viewModel.actors.map(a => actorPainter.paint(a, pointMap))

    val activityCanvas = for {
      a <- viewModel.actors
      activity <- a.activities
    } yield activityPainter.paint(activity, pointMap)

    val signalCanvas = for {
      a <- viewModel.actors
      activity <- a.activities
      rightPoint <- activity.rightPoints
    } yield
      rightPoint.signalComponent match {
        case x: AutoSignalComponent => autoSignalPainter.paint(x, pointMap)
        case x: AsyncRequest        => asyncRequestPainter.paint(x, pointMap)
        case x: SyncRequest         => syncRequestPainter.paint(x, pointMap)
        case x: SyncResponse        => syncResponsePainter.paint(x, pointMap)
      }

//		val alternativeCanvas = for {
//		 a <- viewModel.alternatives
//		} yield xxx

    (actorCanvas ++ activityCanvas ++ signalCanvas)
      .reduce(_.merge(_))
      .merge(titleCanvas)
  }

  private def paintTitle(viewModel: ViewModelComponents, pointMap: ResolvedPoints): FixedWidthCanvas = {
    val sequenceWidth = pointMap(ViewMatrix.width()).x
    val sequenceHeight = pointMap(ViewMatrix.height()).x

    val component = viewModel.sequenceComponents.head
    val sequenceTitle = component.name

    val canvas = new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 0), r("_", sequenceTitle.length + 3))
      .write(Fixed2dPoint(0, 1), s"| ${sequenceTitle} \\")
      .write(Fixed2dPoint(0, 2), "|" + r("-", sequenceWidth))
      .write(Fixed2dPoint(0, sequenceHeight), "|" + r("_", sequenceWidth))

    for (y <- 3 until sequenceHeight.toInt) {
      canvas
        .write(Fixed2dPoint(0, y), "|")
        .write(Fixed2dPoint(sequenceWidth, y), "|")
    }
    canvas
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
