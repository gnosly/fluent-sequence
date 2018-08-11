package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, Point1d}
import org.scalatest.{FunSuite, Matchers}

class Fixed1DPointTest extends FunSuite with Matchers{


	test("ok") {
		resolve(Fixed1DPoint(1) + Fixed1DPoint(2)) shouldBe Fixed1DPoint(3)
		resolve(Fixed1DPoint(2) - Fixed1DPoint(1)) shouldBe Fixed1DPoint(1)
		Fixed1DPoint(2) < Fixed1DPoint(3) shouldBe true
		Fixed1DPoint(2) < Fixed1DPoint(2) shouldBe false
		Fixed1DPoint(2) <=  Fixed1DPoint(2) shouldBe true
		Fixed1DPoint(2) <=  Fixed1DPoint(1) shouldBe false
	}

	def resolve(point: Point1d) = point.resolve(null)
}
