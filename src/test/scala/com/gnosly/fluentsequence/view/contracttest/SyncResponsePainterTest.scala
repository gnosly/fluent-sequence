package com.gnosly.fluentsequence.view.contracttest
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.SyncResponse
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

abstract class SyncResponsePainterTest(painter: ComponentPainter[SyncResponse]) extends FunSuite with Matchers {

  test("backward") {
    val map = ResolvedPoints(
      Map(
        Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
        Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
      ))

    val response = new SyncResponse("sync response", 0, 1, 0, 0, 0)

    val canvas = painter.paint(response, map)

    val print = canvas.print()
    println(print)

    print shouldBe backwardExpected
  }

  test("forward") {
    val map = ResolvedPoints(
      Map(
        Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(0, 0),
        Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(20, 0),
      ))

    val response = new SyncResponse("sync response", 0, 0, 0, 1, 0)

    val canvas = painter.paint(response, map)

    val print = canvas.print()
    println(print)

    print shouldBe forwardExpected
  }

  def backwardExpected: String

  def forwardExpected: String
}
