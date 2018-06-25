package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.{ComponentPainter, Fixed2dPoint}

class SvgActorPainter extends ComponentPainter[ActorComponent] {
	override def paint(actor: ActorComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
		val canvas = new SvgCanvas()

		val padding = 2
		val name = actor.name
		val width = name.length + padding

		val actorTopLeft = pointMap(Actor.topLeft(actor.id))
		val actorBottomMiddle = pointMap(Actor.bottomMiddle(actor.id))

		canvas.drawRect(actorTopLeft, width, (actorBottomMiddle.y - actorTopLeft.y) - 1)

		val fontWidth = 1
		val fontHeight= 2

		canvas.drawText(actorTopLeft.down(fontHeight).right(fontWidth), name)
		canvas
	}
}
