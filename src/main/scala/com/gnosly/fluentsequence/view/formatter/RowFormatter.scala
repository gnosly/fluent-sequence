package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.ViewModels.BiSignalModel
import com.gnosly.fluentsequence.view.model.ViewModels.SignalModel
import com.gnosly.fluentsequence.view.model.point._

class RowFormatter {

  def format(signal: SignalModel): Pointable = signal match {
    case x: AutoSignalModel => format(x)
    case x: BiSignalModel   => format(x)
  }

  def format(signal: AutoSignalModel): Pointable = {
    RowPoint(signal.currentIndex,
             new ReferencePoint(Activity.pointEnd(signal.actorId, signal.activityId, signal.currentIndex, "right")).y)
  }

  def format(signal: BiSignalModel): Pointable = {
    val direction = if (signal.leftToRight) "right" else "left"
    RowPoint(signal.currentIndex,
             new ReferencePoint(
               Activity.pointEnd(signal.fromActorId, signal.fromActivityId, signal.currentIndex, direction)).y)
  }

}

case class RowPoint(signalIndex: Int, rowCoordinate: Point1d) extends Pointable {
  override def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] =
    ViewMatrix.row(signalIndex) -> Fixed2dPoint(rowCoordinate.resolve(pointMap).x, 0) :: Nil
}
