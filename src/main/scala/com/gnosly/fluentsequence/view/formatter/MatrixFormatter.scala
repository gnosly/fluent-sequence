package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point._

class MatrixFormatter(fixedPreRenderer: FixedPreRenderer) {

  def format(actor: ActorComponent): Pointable = {
    val width = fixedPreRenderer.preRender(actor).width

    if (actor.isLast) {
      return MatrixPoint(Fixed1DPoint(width / 2))
    }

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(0)).x

    val result: Point1d = actor.activities
      .flatMap(a => a.rightPoints)
      .map(_._2)
      .foldLeft[Point1d](Fixed1DPoint(width + DISTANCE_BETWEEN_ACTORS))((acc, e) => {

        e.signalComponent match {
          case x: BiSignalComponent   => bisignal(acc, x, actorStartX)
          case x: AutoSignalComponent => auto(acc, x, actorStartX)
        }
      })

    MatrixPoint(result)
  }

  def auto(acc: Point1d, signal: AutoSignalComponent, actorStartX: Point1d): Point1d = {
		columnWidthForcedBySignal(acc, signal, actorStartX)
  }

  def bisignal(acc: Point1d, signal: BiSignalComponent, actorStartX: Point1d): Point1d = {
    if (!signal.leftToRight)
      return acc

    columnWidthForcedBySignal(acc, signal, actorStartX)
  }

  private def columnWidthForcedBySignal(
      acc: Point1d,
      signal: SignalComponent,
      actorStartX: Point1d) = {
    val signalWidth = fixedPreRenderer.preRender(signal).width

    val signalStartX =
      new ReferencePoint(Activity.pointStart(signal.fromActorId, signal.fromActivityId, signal.currentIndex, "right")).x

    val columnWidthForcedBySignal = signalStartX - actorStartX + Fixed1DPoint(signalWidth)

    acc max columnWidthForcedBySignal
  }
}

case class MatrixPoint(point1d: Point1d) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = ???
}
