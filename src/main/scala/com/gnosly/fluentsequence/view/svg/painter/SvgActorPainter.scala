package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgActorPainter extends ComponentPainter[ActorComponent] {
  override def paint(actor: ActorComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
    val canvas = new SvgCanvas()

    val padding = 2
    val name = actor.name
    val width = name.length + padding

    val actorTopLeft = pointMap(Actor.topLeft(actor.id))
    val actorBottomMiddle = pointMap(Actor.bottomMiddle(actor.id))

    canvas.drawRect(actorTopLeft, width, (actorBottomMiddle.y - actorTopLeft.y) - 1)
    canvas.drawText(actorBottomMiddle.up(2), name, "middle")
    canvas
  }
}
