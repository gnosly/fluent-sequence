package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FlatSpec, Matchers}

class VeryFixed2dPointTest extends FlatSpec with Matchers {

	it should "compute down" in {
		new VeryFixed2dPoint(0,0).down(1) shouldBe new VeryFixed2dPoint(0,1)
		new VeryFixed2dPoint(0,0).down(5) shouldBe new VeryFixed2dPoint(0,5)
	}

	it should "compute right" in {
		new VeryFixed2dPoint(0,0).right(1) shouldBe new VeryFixed2dPoint(1,0)
		new VeryFixed2dPoint(0,0).right(6) shouldBe new VeryFixed2dPoint(6,0)
	}

	it should "compute left" in {
		new VeryFixed2dPoint(10,0).left(1) shouldBe new VeryFixed2dPoint(9,0)
		new VeryFixed2dPoint(10,0).left(6) shouldBe new VeryFixed2dPoint(4,0)
	}
}
