package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point._

class MatrixFormatter(fixedPreRenderer: FixedPreRenderer) {
  def format(actor: ActorComponent): Pointable = {
		val width = fixedPreRenderer.preRender(actor).width

		if (actor.isLast){
			MatrixPoint(Fixed1DPoint(width / 2))
		}else{
			MatrixPoint(Fixed1DPoint(width + DISTANCE_BETWEEN_ACTORS))
		}

//		var point = Fixed1DPoint(0)
//		if (direction == "right") {
//			val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(actor.id)).x
//			val signalStartX = new ReferencePoint(Coordinates.Activity.topLeft()
//			po
//



  }
}

case class MatrixPoint(point1d: Point1d) extends Pointable {
	override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = ???
}