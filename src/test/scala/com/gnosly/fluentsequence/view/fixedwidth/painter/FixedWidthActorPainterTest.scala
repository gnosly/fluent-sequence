package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.Coordinates
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2dPoint, FixedWidthCanvas}
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import org.scalatest.{FunSuite, Matchers}

class FixedWidthActorPainterTest extends FunSuite with Matchers {
  val painter = new FixedWidthActorPainter()

  test("render user box") {
    val user = new ActorComponent(0, "user")
    user.done("something", 0)
    val canvas = new FixedWidthCanvas()

    painter.paint(user, canvas, Map(
      Coordinates.Actor.topLeft(0) -> new Fixed2dPoint(1, 0),
      Coordinates.Actor.bottomMiddle(0) -> new Fixed2dPoint(4, 4),
    ))

    val print = canvas.print
    println(print)
    print shouldBe
      /**/ " .------." + "\n" +
      /**/ " | user |" + "\n" +
      /**/ " '------'" + "\n" +
      /**/ "    |"

  }
}