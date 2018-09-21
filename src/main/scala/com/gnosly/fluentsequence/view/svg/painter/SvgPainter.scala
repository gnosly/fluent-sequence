package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.component.{AsyncRequest, AutoSignalComponent, SyncRequest, SyncResponse}
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.{Canvas, Painter, ViewModelComponents}

//fixme maybe could be one painter
case class SvgPainter() extends Painter {
  val actorPainter = new SvgActorPainter
  val activityPainter = new SvgActivityPainter
  val syncRequestPainter = new SvgSyncRequestPainter
  val syncResponsePainter = new SvgSyncResponsePainter
  val asyncRequestPainter = new SvgAsyncRequestPainter
  val autoSignalPainter = new SvgAutoSignalPainter

  override def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas = {

    val actorCanvas = viewModel._actors.map(a => actorPainter.paint(a._2, pointMap))

    val activityCanvas = for {
      a <- viewModel._actors
      activity <- a._2.activities
    } yield activityPainter.paint(activity, pointMap)

    val signalCanvas = for {
      a <- viewModel._actors
      activity <- a._2.activities
      rightPoint <- activity.rightPoints
    } yield
      rightPoint._2.signalComponent match {
        case x: AutoSignalComponent => autoSignalPainter.paint(x, pointMap)
        case x: SyncRequest         => syncRequestPainter.paint(x, pointMap)
        case x: SyncResponse         => syncResponsePainter.paint(x, pointMap)
        case x: AsyncRequest         => asyncRequestPainter.paint(x, pointMap)
      }

    (actorCanvas ++ activityCanvas ++ signalCanvas)
      .reduce(_.merge(_))
  }
}
