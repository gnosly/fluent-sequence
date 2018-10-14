package com.gnosly.fluentsequence.view.model.point

import org.scalatest.FunSuite
import org.scalatest.Matchers

class ReferencePointTest extends FunSuite with Matchers {

  test("referencePoint") {
    val pointMap = ResolvedPoints(Map("refPoint" -> Fixed2dPoint(0, 0)))
    val point = new ReferencePoint("refPoint")

    point.resolve(pointMap) shouldBe Fixed2dPoint(0, 0)
    point.atY(3).resolve(pointMap) shouldBe Fixed2dPoint(0, 3)
    point.down(1).resolve(pointMap) shouldBe Fixed2dPoint(0, 1)
    point.up(1).resolve(pointMap) shouldBe Fixed2dPoint(0, -1)
    point.right(1).resolve(pointMap) shouldBe Fixed2dPoint(1, 0)
    point.left(1).resolve(pointMap) shouldBe Fixed2dPoint(-1, 0)
    point.x.resolve(pointMap) shouldBe Fixed1DPoint(0)
    point.y.resolve(pointMap) shouldBe Fixed1DPoint(0)
  }
}
