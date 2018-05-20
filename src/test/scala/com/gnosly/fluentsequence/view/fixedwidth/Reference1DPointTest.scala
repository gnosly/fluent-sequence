package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class Reference1DPointTest extends FunSuite  with Matchers{

	test("ok"){
		val pointMap = new PointMap()
		val point = new ReferencePoint("refPoint")
		pointMap.putAll( ("refPoint", new VeryFixed2dPoint(2,0) ) :: Nil)


		Reference1DPoint("refPoint").resolve(pointMap) shouldBe Fixed1DPoint(2)
	}
}