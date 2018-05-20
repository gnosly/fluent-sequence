package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class Fixed2DPointTest extends FunSuite with Matchers {

	test("ok"){
		resolve(Fixed2DPoint(Fixed1DPoint(1), Fixed1DPoint(2)).right(1)) shouldBe VeryFixed2dPoint(2,2)
		resolve(Fixed2DPoint(Fixed1DPoint(1), Fixed1DPoint(2)).left(1)) shouldBe VeryFixed2dPoint(0,2)
	}

	def resolve(point2d: Point2d): VeryFixed2dPoint ={
		point2d.resolve(null)
	}
}
