package com.gnosly.fluentsequence.view.model.point

import org.scalatest.{FunSuite, Matchers}

class Variable1DPointTest extends FunSuite with Matchers {

  test("ok") {
    val pointMap = new PointMap()
    val point = new ReferencePoint("refPoint")
    pointMap.putAll(("refPoint", new Fixed2dPoint(2, 0)) :: Nil)

    Variable1DPoint(Reference1DPoint("refPoint"), Fixed1DPoint(5), (x, y) => Fixed1DPoint(x.x + y.x))
      .resolve(pointMap) shouldBe Fixed1DPoint(7)
  }
}
