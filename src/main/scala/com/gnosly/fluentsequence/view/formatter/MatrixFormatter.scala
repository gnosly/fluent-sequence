package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point._

class MatrixFormatter(fixedPreRenderer: FixedPreRenderer) {
  def format(actor: ActorComponent): Pointable = {
		val midWidth = fixedPreRenderer.preRender(actor).width / 2

//		val nextActorTopLeftCorner = new ReferencePoint(Coordinates.Actor.topLeft(actor.id + 1)).resolve(pointMap)
//
//		if (nextActorTopLeftCorner == Fixed2dPoint(0L, 0L))

//		else
//			Fixed1DPoint(actorBox.width + FormatterConstants.DISTANCE_BETWEEN_ACTORS)


//		var point = Fixed1DPoint(0)
//		if (direction == "right") {
//			val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(actor.id)).x
//			val signalStartX = new ReferencePoint(Coordinates.Activity.topLeft()
//			po
//


    new MatrixPoint(Fixed1DPoint(midWidth))
  }
}

case class MatrixPoint(point1d: Point1d) extends Pointable {
	override def toPoints(pointMap: PointMap): Seq[(String, Fixed2dPoint)] = ???
}