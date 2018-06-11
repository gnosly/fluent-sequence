package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FlatSpec, Matchers}

class Fixed2DPointTest extends FlatSpec with Matchers {

	it should "compute down" in {
		new Fixed2dPoint(0,0).down(1) shouldBe new Fixed2dPoint(0,1)
		new Fixed2dPoint(0,0).down(5) shouldBe new Fixed2dPoint(0,5)
	}

	it should "compute right" in {
		new Fixed2dPoint(0,0).right(1) shouldBe new Fixed2dPoint(1,0)
		new Fixed2dPoint(0,0).right(6) shouldBe new Fixed2dPoint(6,0)
	}

	it should "compute left" in {
		new Fixed2dPoint(10,0).left(1) shouldBe new Fixed2dPoint(9,0)
		new Fixed2dPoint(10,0).left(6) shouldBe new Fixed2dPoint(4,0)
	}
}
