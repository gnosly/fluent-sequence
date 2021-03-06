package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SvgAutoSignalPainterTest extends FunSuite with Matchers {
  val painter = new SvgAutoSignalPainter

  test("simple autosignal") {

    val pointMap = new ResolvedPoints(Map(Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2dPoint(1, 10)))

    val canvas = painter.paint(AutoSignalModel("name", 0, 0, 0), pointMap)
    println(canvas)
    canvas.content shouldBe """<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="0,100 20,100 20,130 2,130"/>
																|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="12,125 2,130 12,135"/>
																|<text x="40" y="120" font-size="16px" text-anchor="start">name</text>
																|""".stripMargin

  }
}
