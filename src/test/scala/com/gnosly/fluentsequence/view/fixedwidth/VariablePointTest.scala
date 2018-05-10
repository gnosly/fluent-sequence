package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class VariablePointTest extends FunSuite with Matchers {

	test("default behaviour") {
		val pointMap = new PointMap()
		pointMap.put("refPoint", Fixed2DPoint(0, 10))
		val point = VariablePoint(ReferencePoint("refPoint"), Fixed1DPoint(0), _.atY(_))

		point.resolve(pointMap, null) shouldBe Fixed2DPoint(0, 0)
		point.atY(11).resolve(pointMap, null) shouldBe Fixed2DPoint(0, 11)
		point.down(1).resolve(pointMap, null) shouldBe Fixed2DPoint(0, 1)
		point.up(1).resolve(pointMap, null) shouldBe Fixed2DPoint(0, -1)
		point.right(1).resolve(pointMap, null) shouldBe Fixed2DPoint(1, 0)
		point.left(1).resolve(pointMap, null) shouldBe Fixed2DPoint(-1, 0)
	}
}
