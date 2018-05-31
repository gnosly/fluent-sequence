package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.Actor
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.fixedwidth.PointMath.max
import com.gnosly.fluentsequence.view.model.ActorComponent
import org.scalatest.{FunSuite, Matchers}

class FixedWidthActorFormatterTest extends FunSuite with Matchers {
	val formatter = new FixedWidthActorFormatter(new FixedWidthPainter())

	test("format first actor alone") {
		formatter.format(new ActorComponent(0, "user", null)) shouldBe ActorPoints(0, new Fixed2DPoint(1, 1), Box(8, 4))
	}

	test("format second actor alone") {
		formatter.format(new ActorComponent(1, "user", null)) shouldBe
			ActorPoints(1,
				new ReferencePoint(Actor.topRight(0))
					.right(max(Reference1DPoint(s"column_${0}"), Fixed1DPoint(DISTANCE_BETWEEN_ACTORS))),
				Box(8, 4)
			)
	}

}
