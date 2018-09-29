package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SvgCanvasTest extends FunSuite with Matchers {
  val CANVAS_CONTENT = "canvassss"

  test("canvas") {
    new SvgCanvas(CANVAS_CONTENT).content shouldBe CANVAS_CONTENT
  }

  test("draw text") {
    val content = new SvgCanvas()
      .drawText(Fixed2dPoint(0, 0), "text")
      .content

    content shouldBe
      """<text x="0" y="0" font-size="16px" text-anchor="start">text</text>
				|""".stripMargin
  }

  test("merge") {
    val canvasA = new SvgCanvas("A")
    val canvasB = new SvgCanvas("B")
    canvasA.merge(canvasB).content shouldBe """AB"""
  }

}
