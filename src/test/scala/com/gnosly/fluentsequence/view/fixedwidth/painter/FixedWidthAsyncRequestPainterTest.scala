package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.AsyncRequest
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap

class FixedWidthAsyncRequestPainterTest extends FunSuite with Matchers {
  val painter = new FixedWidthAsyncRequestPainter()

  test("left to right") {
    val map = new ResolvedPoints(
      Map(
        Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
        Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
      ))

    val request = new AsyncRequest("async request", 0, 0, 0, 1, 0)

    val canvas = painter.paint(request, map)

    val print = canvas.print()
    println(print)
    print shouldBe
      /****/ "   async request" + "\n" +
        /**/ " - - - - - - - - ->"
  }
}
