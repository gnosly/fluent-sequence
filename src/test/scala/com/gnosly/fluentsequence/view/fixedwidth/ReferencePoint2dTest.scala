package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class ReferencePoint2dTest extends FunSuite with Matchers{

	test("referencePoint"){
		val pointMap = new PointMap()
		val point = ReferencePoint("refPoint")
		pointMap.putAll( ("refPoint", Fixed2DPoint(0,0) ) :: Nil)

		point.resolve(pointMap) shouldBe Fixed2DPoint(0,0)
		point.atY(3).resolve(pointMap) shouldBe Fixed2DPoint(0,3)
		point.down(1).resolve(pointMap) shouldBe Fixed2DPoint(0,1)
		point.up(1).resolve(pointMap) shouldBe Fixed2DPoint(0,-1)
		point.right(1).resolve(pointMap) shouldBe Fixed2DPoint(1,0)
		point.left(1).resolve(pointMap) shouldBe Fixed2DPoint(-1,0)
		point.x().resolve(pointMap) shouldBe Fixed1DPoint(0)
		point.y().resolve(pointMap) shouldBe Fixed1DPoint(0)
//		point.y().resolve(pointMap, null) shouldBe Fixed1DPoint(0)
	}
}
