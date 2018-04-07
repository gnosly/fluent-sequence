package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FlatSpec, Matchers}

class Fixed2DPointTest extends FlatSpec with Matchers {

	it should "compute down" in {
		Fixed2DPoint(0,0).down(1) shouldBe Fixed2DPoint(0,1)
		Fixed2DPoint(0,0).down(5) shouldBe Fixed2DPoint(0,5)
	}

	it should "compute right" in {
		Fixed2DPoint(0,0).right(1) shouldBe Fixed2DPoint(1,0)
		Fixed2DPoint(0,0).right(6) shouldBe Fixed2DPoint(6,0)
	}

	it should "compute left" in {
		Fixed2DPoint(10,0).left(1) shouldBe Fixed2DPoint(9,0)
		Fixed2DPoint(10,0).left(6) shouldBe Fixed2DPoint(4,0)
	}
}
