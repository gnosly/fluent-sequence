package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point._

class ColumnFormatter(fixedPreRenderer: FixedPreRenderer) {

  def format(actor: ActorComponent): Pointable = {
    val minWidth = widthForcedBy(actor)

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(actor.id)).x

    val result: Point1d = actor.activities
      .flatMap(a => a.rightPoints)
      .map(_._2)
      .foldLeft[Point1d](minWidth)((acc, e) => {
        e.signalComponent match {
          case x: BiSignalComponent   => acc max columnWidthForcedByBiSignal(x, actorStartX)
          case x: AutoSignalComponent => acc max columnWidthForcedByAutoSignal(x, actorStartX)
        }

      })

    ColumnPoint(actor.id, result)
  }

  private def widthForcedBy(actor: ActorComponent) = {
    val width = fixedPreRenderer.preRender(actor).width

    if (actor.isLast) {
      Fixed1DPoint(width / 2)
    } else {
      Fixed1DPoint(width + DISTANCE_BETWEEN_ACTORS)
    }
  }

  private def columnWidthForcedByAutoSignal(signal: AutoSignalComponent, actorStartX: Point1d) = {
    val signalWidth = fixedPreRenderer.preRender(signal).width

    val actorId = signal.fromActorId
    val activityId = signal.fromActivityId

    val signalStartX =
      new ReferencePoint(Activity.pointStart(actorId, activityId, signal.currentIndex, "right")).x

    signalStartX - actorStartX + Fixed1DPoint(signalWidth)
  }

  private def columnWidthForcedByBiSignal(signal: BiSignalComponent, actorStartX: Point1d) = {
    val signalWidth = fixedPreRenderer.preRender(signal).width

    var actorId = 0
    var activityId = 0

    if (signal.leftToRight) {
      actorId = signal.fromActorId
      activityId = signal.fromActivityId
    } else {
      actorId = signal.toActorId
      activityId = signal.toActivityId
    }

    val signalStartX =
      new ReferencePoint(Activity.pointStart(actorId, activityId, signal.currentIndex, "right")).x

    signalStartX - actorStartX + Fixed1DPoint(signalWidth)
  }
}

case class ColumnPoint(actorId: Int, columnWidth: Point1d) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.column(actorId) -> Fixed2dPoint(columnWidth.resolve(pointMap).x, 0) :: Nil
}
