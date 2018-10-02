package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.model.component.SignalComponent
import com.gnosly.fluentsequence.view.model.point._

class RowFormatter {

  def format(signal: SignalComponent): Pointable = signal match {
    case x: AutoSignalComponent => format(x)
    case x: BiSignalComponent   => format(x)
  }

  def format(signal: AutoSignalComponent): Pointable = {
    RowPoint(
      signal.currentIndex,
      new ReferencePoint(Activity.pointEnd(signal.fromActorId, signal.fromActivityId, signal.currentIndex, "right")).y)
  }

  def format(signal: BiSignalComponent): Pointable = {
    val direction = if (signal.leftToRight) "right" else "left"
    RowPoint(signal.currentIndex,
             new ReferencePoint(
               Activity.pointEnd(signal.fromActorId, signal.fromActivityId, signal.currentIndex, direction)).y)
  }

}

case class RowPoint(signalIndex: Int, rowCoordinate: Point1d) extends Pointable {
  override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.row(signalIndex) -> Fixed2dPoint(rowCoordinate.resolve(pointMap).x, 0) :: Nil
}
