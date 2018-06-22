package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.Coordinates.Actor
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2dPoint, FixedWidthCanvas}
import com.gnosly.fluentsequence.view.model.component.ActorComponent

class FixedWidthActorPainter {

  def paint(actor: ActorComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
    val padding = 2
    val name = actor.name
    val innerSize = name.length + padding

    val actorTopLeft = pointMap(Actor.topLeft(actor.id))
    val actorBottomMiddle = pointMap(Actor.bottomMiddle(actor.id))

    val str = r("-", innerSize)

    canvas.write(actorTopLeft, "." + str + ".")
    canvas.write(actorTopLeft.down(1), "| " + name + " |")
    canvas.write(actorTopLeft.down(2), "'" + str + "'")
    canvas.write(actorBottomMiddle.up(1), "|")

  }

  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)
}
