package com.gnosly.fluentsequence.view.model.point

import org.scalatest.{FunSuite, Matchers}

class Variable2DPointTest extends FunSuite with Matchers {

	test("ok"){
		resolve(Variable2DPoint(Fixed1DPoint(1), Fixed1DPoint(2)).right(1)) shouldBe Fixed2dPoint(2,2)
		resolve(Variable2DPoint(Fixed1DPoint(1), Fixed1DPoint(2)).left(1)) shouldBe Fixed2dPoint(0,2)
	}

	def resolve(point2d: Point2d): Fixed2dPoint ={
		point2d.resolve(null)
	}
}
