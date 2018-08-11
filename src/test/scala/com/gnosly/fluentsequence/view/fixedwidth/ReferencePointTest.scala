package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, Fixed2dPoint, PointMap, ReferencePoint}
import org.scalatest.{FunSuite, Matchers}

class ReferencePointTest extends FunSuite with Matchers{

	test("referencePoint"){
		val pointMap = new PointMap()
		val point = new ReferencePoint("refPoint")
		pointMap.putAll( ("refPoint", Fixed2dPoint(0,0) ) :: Nil)

		point.resolve(pointMap) shouldBe Fixed2dPoint(0,0)
		point.atY(3).resolve(pointMap) shouldBe Fixed2dPoint(0,3)
		point.down(1).resolve(pointMap) shouldBe Fixed2dPoint(0,1)
		point.up(1).resolve(pointMap) shouldBe Fixed2dPoint(0,-1)
		point.right(1).resolve(pointMap) shouldBe Fixed2dPoint(1,0)
		point.left(1).resolve(pointMap) shouldBe Fixed2dPoint(-1,0)
		point.x().resolve(pointMap) shouldBe Fixed1DPoint(0)
		point.y().resolve(pointMap) shouldBe Fixed1DPoint(0)
	}
}
