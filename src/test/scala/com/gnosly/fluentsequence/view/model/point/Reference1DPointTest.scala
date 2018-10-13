package com.gnosly.fluentsequence.view.model.point

import org.scalatest.FunSuite
import org.scalatest.Matchers

class Reference1DPointTest extends FunSuite with Matchers {

  test("ok") {
    val pointMap = new ResolvedPoints(Map("refPoint" -> Fixed2dPoint(2, 0)))

    Reference1DPoint("refPoint").resolve(pointMap) shouldBe Fixed1DPoint(2)
  }

  test("coordinates") {
    val resolvedPoints = new ResolvedPoints(Map("refPoint" -> Fixed2dPoint(2, 10)))

    Reference1DPoint("refPoint#x").resolve(resolvedPoints) shouldBe Fixed1DPoint(2)
    Reference1DPoint("refPoint#y").resolve(resolvedPoints) shouldBe Fixed1DPoint(10)
  }
}
