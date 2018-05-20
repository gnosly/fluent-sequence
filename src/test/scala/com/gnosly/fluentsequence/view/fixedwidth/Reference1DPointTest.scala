package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class Reference1DPointTest extends FunSuite  with Matchers{

	test("ok"){
		val pointMap = new PointMap()
		pointMap.putAll( ("refPoint", new VeryFixed2dPoint(2,0) ) :: Nil)

		Reference1DPoint("refPoint").resolve(pointMap) shouldBe Fixed1DPoint(2)
	}

	test("coordinates"){
		val pointMap = new PointMap()
		pointMap.putAll( ("refPoint", new VeryFixed2dPoint(2,10) ) :: Nil)

		pointMap.get1DPoint("refPoint#x") shouldBe Fixed1DPoint(2)
		pointMap.get1DPoint("refPoint#y") shouldBe Fixed1DPoint(10)
	}
}
