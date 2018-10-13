package com.gnosly.fluentsequence.view.svg.painter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.SyncResponse
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap

class SvgSyncResponsePainterTest extends FunSuite with Matchers {

  val painter = new SvgSyncResponsePainter

  test("bisignal right to left") {
    val pointMap = new ResolvedPoints(
      Map(
        Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(1, 10),
        Coordinates.Activity.leftPointStart(1, 0, 0) -> Fixed2dPoint(10, 10)
      ))

    val canvas = painter.paint(new SyncResponse("name", 0, 1, 0, 0, 0), pointMap)
    println(canvas)
    canvas.content shouldBe
      """<text x="30" y="100" font-size="16px" text-anchor="start">name</text>
				|<line x1="2" y1="110" x2="100" y2="110" style="stroke:black;stroke-width:1.5;" />
				|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="12,105 2,110 12,115"/>
				|""".stripMargin
  }
}
