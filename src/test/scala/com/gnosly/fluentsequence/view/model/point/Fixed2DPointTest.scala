package com.gnosly.fluentsequence.view.model.point

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class Fixed2DPointTest extends FlatSpec with Matchers {

  it should "compute down" in {
    Fixed2dPoint(0, 0).down(1) shouldBe Fixed2dPoint(0, 1)
    Fixed2dPoint(0, 0).down(5) shouldBe Fixed2dPoint(0, 5)
  }

  it should "compute right" in {
    Fixed2dPoint(0, 0).right(1) shouldBe Fixed2dPoint(1, 0)
    Fixed2dPoint(0, 0).right(6) shouldBe Fixed2dPoint(6, 0)
  }

  it should "compute left" in {
    Fixed2dPoint(10, 0).left(1) shouldBe Fixed2dPoint(9, 0)
    Fixed2dPoint(10, 0).left(6) shouldBe Fixed2dPoint(4, 0)
  }
}
