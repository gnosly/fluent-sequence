package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthCanvasTest extends FlatSpec with Matchers {
  it should "render single point" in {
    val fixedWidthCanvas = new FixedWidthCanvas()

    fixedWidthCanvas.write(new Fixed2dPoint(0, 0), '-')

    fixedWidthCanvas.print() shouldBe "-"
  }

  it should "render single point along x" in {
    val fixedWidthCanvas = new FixedWidthCanvas()

    fixedWidthCanvas.write(new Fixed2dPoint(2, 0), '-')

    fixedWidthCanvas.print() shouldBe "  -"
  }

  it should "render single point along y" in {
    val fixedWidthCanvas = new FixedWidthCanvas()

    fixedWidthCanvas.write(new Fixed2dPoint(0, 2), '-')

    fixedWidthCanvas.print() shouldBe "\n" +
      "\n" +
      "-"
  }

  it should "render two points " in {
    val fixedWidthCanvas = new FixedWidthCanvas()

    fixedWidthCanvas.write(new Fixed2dPoint(0, 0), '1')
    fixedWidthCanvas.write(new Fixed2dPoint(3, 0), '2')

    fixedWidthCanvas.print() shouldBe "1  2"
  }

  it should "render two points in inverse order " in {
    val fixedWidthCanvas = new FixedWidthCanvas()

    fixedWidthCanvas.write(new Fixed2dPoint(3, 0), '2')
    fixedWidthCanvas.write(new Fixed2dPoint(0, 0), '1')

    fixedWidthCanvas.print() shouldBe "1  2"
  }

  it should "render multi points " in {
    val fixedWidthCanvas = new FixedWidthCanvas()

    fixedWidthCanvas.write(new Fixed2dPoint(0, 0), '1')
    fixedWidthCanvas.write(new Fixed2dPoint(3, 0), '2')
    fixedWidthCanvas.write(new Fixed2dPoint(0, 3), '3')
    fixedWidthCanvas.write(new Fixed2dPoint(3, 3), '4')

    fixedWidthCanvas.print() shouldBe "1  2\n" +
      "\n" +
      "\n" +
      "3  4"
  }

  it should "render a string" in {
    val fixedWidthCanvas = new FixedWidthCanvas()
    fixedWidthCanvas.write(new Fixed2dPoint(0, 0), "1  2")
    fixedWidthCanvas.write(new Fixed2dPoint(0, 3), "3  4")

    fixedWidthCanvas.print() shouldBe "1  2\n" +
      "\n" +
      "\n" +
      "3  4"
  }

  it should "merge with other canvas" in {
    val canvasA = new FixedWidthCanvas()
    val canvasB = new FixedWidthCanvas()

    canvasA.write(new Fixed2dPoint(0, 0), "1  2")
    canvasB.write(new Fixed2dPoint(0, 3), "3  4")

    canvasA.merge(canvasB).print() shouldBe "1  2\n" +
      "\n" +
      "\n" +
      "3  4"
  }
}
