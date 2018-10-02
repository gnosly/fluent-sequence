package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.component.SyncRequest
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap

class FixedWidthSyncRequestPainterTest extends FunSuite with Matchers {

  val painter = new FixedWidthSyncRequestPainter()

  test("left to right") {
    val map = HashMap(
      Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
      Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
    )

    val canvas = painter.paint(new SyncRequest("sync request", 0, 0, 0, 1, 0), map)

    val print = canvas.print()
    println(print)
    print shouldBe
      /****/ "    sync request" + "\n" +
        /**/ "------------------->"
  }
}
