package com.gnosly.fluentsequence.view
import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.SyncRequest
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SyncRequestPainterTest extends FunSuite with Matchers {

  val forwardTest = (painter: ComponentPainter[SyncRequest], expectedCanvas: Canvas) =>
    test("forward") {
      val map = ResolvedPoints(
        Map(
          Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
          Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
        ))

      val request = new SyncRequest("sync request", 0, 0, 0, 1, 0)

      val canvas = painter.paint(request, map)

      assertEquals(expectedCanvas, canvas)
  }

  val backwardTest = (painter: ComponentPainter[SyncRequest], expectedCanvas: Canvas) =>
    test("backward") {
      val map = ResolvedPoints(
        Map(
          Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
          Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
        ))

      val request = new SyncRequest("sync request", 0, 1, 0, 0, 0)

      val canvas = painter.paint(request, map)

      assertEquals(expectedCanvas, canvas)
  }

  private def assertEquals(expectedCanvas: Canvas, canvas: Canvas) = {
    val print = canvas.print()
    val expectedPrint = expectedCanvas.print()
    println(print)

    print shouldBe expectedPrint
  }

}
