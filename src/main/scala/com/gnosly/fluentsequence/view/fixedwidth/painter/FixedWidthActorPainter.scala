package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class FixedWidthActorPainter extends ComponentPainter[ActorComponent] {

  override def paint(actor: ActorComponent, pointMap: Map[String, Fixed2dPoint]): FixedWidthCanvas = {
    val padding = 2
    val name = actor.name
    val innerSize = name.length + padding

    val actorTopLeft = pointMap(Actor.topLeft(actor.id))
    val actorBottomMiddle = pointMap(Actor.bottomMiddle(actor.id))

    val str = r("-", innerSize)

    new FixedWidthCanvas()
      .write(actorTopLeft, "." + str + ".")
      .write(actorTopLeft.down(1), "| " + name + " |")
      .write(actorTopLeft.down(2), "'" + str + "'")
      .write(actorBottomMiddle.up(1), "|")
  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)
}
