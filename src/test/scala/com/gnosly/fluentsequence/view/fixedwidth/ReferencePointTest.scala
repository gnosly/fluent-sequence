package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class ReferencePointTest extends FunSuite with Matchers{

	test("referencePoint"){
		val pointMap = new PointMap()
		val point = ReferencePoint("refPoint")
		pointMap.put("refPoint", Fixed2DPoint(0,0))

		point.resolve(pointMap, null) shouldBe Fixed2DPoint(0,0)
		point.atY(3).resolve(pointMap, null) shouldBe Fixed2DPoint(0,3)
		point.down(1).resolve(pointMap, null) shouldBe Fixed2DPoint(0,1)
		point.up(1).resolve(pointMap, null) shouldBe Fixed2DPoint(0,-1)
		point.right(1).resolve(pointMap, null) shouldBe Fixed2DPoint(1,0)
		point.left(1).resolve(pointMap, null) shouldBe Fixed2DPoint(-1,0)
	}
}
