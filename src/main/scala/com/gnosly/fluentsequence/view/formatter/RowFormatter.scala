package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates.{Activity, Pointable}
import com.gnosly.fluentsequence.view.model.component.SignalComponent
import com.gnosly.fluentsequence.view.model.point._

class RowFormatter {

	def format(signal: SignalComponent): Pointable = {
			RowPoint(signal.currentIndex, new ReferencePoint(Activity.pointEnd(signal.fromActorId, signal.fromActivityId, signal.currentIndex, "right")).y)
	}
}

case class RowPoint(signalIndex:Int, point1d: Point1d) extends Pointable{
  override def toPoints(pointMap: PointMap)
    : Seq[(String, Fixed2dPoint)] = ???
}