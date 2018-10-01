package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.component.SignalComponent
import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, Fixed2dPoint, Point1d, PointMap}

class RowFormatter(fixedPreRenderer: FixedPreRenderer) {

	def format(signal: SignalComponent): Pointable = {
		val height = fixedPreRenderer.preRender(signal).height
			RowPoint(signal.currentIndex, Fixed1DPoint(height))
	}
}

case class RowPoint(signalIndex:Int, point1d: Point1d) extends Pointable{
  override def toPoints(pointMap: PointMap)
    : Seq[(String, Fixed2dPoint)] = ???
}