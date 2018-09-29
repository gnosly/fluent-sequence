package com.gnosly.fluentsequence.view.model.point

import org.scalatest.FunSuite
import org.scalatest.Matchers

class Variable1DPointTest extends FunSuite with Matchers {

  test("ok") {
    val pointMap = new PointMap
    pointMap.putAll(("refPoint", Fixed2dPoint(2, 0)) :: Nil)

    Variable1DPoint(Reference1DPoint("refPoint"), Fixed1DPoint(5), (x, y) => Fixed1DPoint(x.x + y.x))
      .resolve(pointMap) shouldBe Fixed1DPoint(7)
  }
}
