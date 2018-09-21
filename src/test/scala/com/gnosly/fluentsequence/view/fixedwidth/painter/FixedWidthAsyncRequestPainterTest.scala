package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.component.AsyncRequest
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.HashMap

class FixedWidthAsyncRequestPainterTest extends FunSuite with Matchers {
  val painter = new FixedWidthAsyncRequestPainter()

  test("left to right") {
    val map: Map[String, Fixed2dPoint] = HashMap(
      Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
      Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
    )

    val canvas = painter.paint(new AsyncRequest("async request", 0, 0, 0, 1, 0), map)

    val print = canvas.print()
    println(print)
    print shouldBe
      /****/ "   async request" + "\n" +
        /**/ " - - - - - - - - ->"
  }
}
