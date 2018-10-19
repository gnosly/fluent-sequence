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

  test("dimension") {
    new SvgCanvas().print shouldBe
      """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="0" width="0"><rect width="100%" height="100%" fill="white"/>""" + "\n" + """</svg>""" + "\n"

    new SvgCanvas(width = 100, height = 200).print shouldBe
      """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="200" width="100"><rect width="100%" height="100%" fill="white"/>""" + "\n" + """</svg>""" + "\n"

    new SvgCanvas().withSize(100, 200).print shouldBe
      """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="2000" width="1000"><rect width="100%" height="100%" fill="white"/>""" + "\n" + """</svg>""" + "\n"
  }

  test("box") {
    val content = new SvgCanvas()
      .drawBox(Fixed2dPoint(0, 0), 50, 70, "condition of alternative")
      .content

    println(content)

    content shouldBe
      """<rect x="0" y="0" width="500" height="700" style="stroke-width: 2.0;stroke: black;fill: transparent" />
				|<rect x="1" y="1" width="191" height="29" style="stroke-width: 0;stroke: black;fill: white" />
				|<text x="10" y="20" font-size="16px" text-anchor="start">condition of alternative</text>
				|<line x1="0" y1="30" x2="192" y2="30" style="stroke:black;stroke-width:1.5;" />
				|<line x1="192" y1="30" x2="212" y2="0" style="stroke:black;stroke-width:1.5;" />""".stripMargin

  }

}
