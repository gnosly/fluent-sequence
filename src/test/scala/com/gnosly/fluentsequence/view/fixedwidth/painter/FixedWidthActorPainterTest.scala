package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.model.ActorModel
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FixedWidthActorPainterTest extends FunSuite with Matchers {
  val painter = new FixedWidthActorPainter

  test("render user box") {
    val user = new ActorModel(0, "user")

    val canvas = painter.paint(user,
                               Map(
                                 Coordinates.Actor.topLeft(0) -> Fixed2dPoint(1, 0),
                                 Coordinates.Actor.bottomMiddle(0) -> Fixed2dPoint(4, 4),
                               ))

    val print = canvas.print()
    println(print)
    print shouldBe
      /**/ " .------." + "\n" +
        /**/ " | user |" + "\n" +
        /**/ " '------'" + "\n" +
        /**/ "    |"

  }
}
