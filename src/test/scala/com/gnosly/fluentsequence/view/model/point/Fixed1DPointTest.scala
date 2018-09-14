package com.gnosly.fluentsequence.view.model.point

import org.scalatest.{FunSuite, Matchers}

class Fixed1DPointTest extends FunSuite with Matchers {

  test("ok") {
    resolve(Fixed1DPoint(1) + Fixed1DPoint(2)) shouldBe Fixed1DPoint(3)
    resolve(Fixed1DPoint(2) - Fixed1DPoint(1)) shouldBe Fixed1DPoint(1)
    Fixed1DPoint(2) < Fixed1DPoint(3) shouldBe true
    Fixed1DPoint(2) < Fixed1DPoint(2) shouldBe false
    Fixed1DPoint(2) <= Fixed1DPoint(2) shouldBe true
    Fixed1DPoint(2) <= Fixed1DPoint(1) shouldBe false
    Fixed1DPoint(2) max Fixed1DPoint(1) shouldBe Fixed1DPoint(2)
    Fixed1DPoint(2) max Fixed1DPoint(3) shouldBe Fixed1DPoint(3)
  }

  def resolve(point: Point1d) = point.resolve(null)
}
