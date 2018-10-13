package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap

class FixedWidthSyncResponsePainterTest extends FunSuite with Matchers {
  val painter = new FixedWidthSyncResponsePainter()

  test("right to left") {
    val map = new ResolvedPoints(
      Map(
        Coordinates.Activity.rightPointStart(1, 0, 0) -> Fixed2dPoint(0, 0),
        Coordinates.Activity.leftPointStart(0, 0, 0) -> Fixed2dPoint(20, 0),
      ))

    val response = new SyncResponse("sync response", 0, 0, 0, 1, 0)

    val canvas = painter.paint(response, map)

    val print = canvas.print()
    println(print)

    print shouldBe
      /****/ "   sync response" + "\n" +
        /**/ "<-------------------"
  }
}
