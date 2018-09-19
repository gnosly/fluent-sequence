package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthCanvasTest extends FlatSpec with Matchers {
  it should "render single point" in {
    new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 0), '-')
      .print() shouldBe "-"
  }

  it should "render single point along x" in {
    new FixedWidthCanvas()
      .write(Fixed2dPoint(2, 0), '-')
      .print() shouldBe "  -"
  }

  it should "render single point along y" in {
    new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 2), '-')
      .print() shouldBe "\n" +
      "\n" +
      "-"
  }

  it should "render two points " in {
    new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 0), '1')
      .write(Fixed2dPoint(3, 0), '2')
      .print() shouldBe "1  2"
  }

  it should "render two points in inverse order " in {
    new FixedWidthCanvas()
      .write(Fixed2dPoint(3, 0), '2')
      .write(Fixed2dPoint(0, 0), '1')
      .print() shouldBe "1  2"
  }

  it should "render multi points " in {
    new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 0), '1')
      .write(Fixed2dPoint(3, 0), '2')
      .write(Fixed2dPoint(0, 3), '3')
      .write(Fixed2dPoint(3, 3), '4')
      .print() shouldBe "1  2\n" +
      "\n" +
      "\n" +
      "3  4"
  }

  it should "render a string" in {
    val fixedWidthCanvas = new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 0), "1  2")
      .write(Fixed2dPoint(0, 3), "3  4")

    fixedWidthCanvas.print() shouldBe "1  2\n" +
      "\n" +
      "\n" +
      "3  4"
  }

  it should "merge with other canvas" in {
    val canvasA = new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 0), "1  2")
    val canvasB = new FixedWidthCanvas()
      .write(Fixed2dPoint(0, 3), "3  4")

    canvasA.merge(canvasB).print() shouldBe "1  2\n" +
      "\n" +
      "\n" +
      "3  4"
  }
}
