package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class VariablePointTest extends FunSuite with Matchers{

	test("sdf"){
		val pointMap = new PointMap()
		pointMap.put("refPoint", Fixed2DPoint(3,0))
		val variablePoint = VariablePoint(ReferencePoint("refPoint"),x => x.atY(2))
		variablePoint.resolve(pointMap) shouldBe Fixed2DPoint(3,2)

		variablePoint.left(2).resolve(pointMap) shouldBe Fixed2DPoint(1,2)

	}

}
