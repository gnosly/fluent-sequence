package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.{Activity, ActivityPoints, Actor, ViewMatrix}
import com.gnosly.fluentsequence.view.fixedwidth.PointMath.max
import com.gnosly.fluentsequence.view.model.ActivityComponent
import org.scalatest.{FunSuite, Matchers}

class FixedWidthActivityFormatterTest extends FunSuite with Matchers {
	val formatter = new FixedWidthActivityFormatter(new FixedWidthPainter())

	test("format first activity") {
		val LAST_SIGNAL_INDEX = 3
		val activity = new ActivityComponent(0, 0, 0, LAST_SIGNAL_INDEX, true, null, null)

		val activityPoints = formatter.format(activity)

		activityPoints shouldBe ActivityPoints(0, 0,
			new ReferencePoint(Actor.bottomMiddle(0)).left(1),
			2,
			Reference1DPoint(ViewMatrix.row(LAST_SIGNAL_INDEX)))
	}

	test("format second activity") {
		val FIRST_SIGNAL_INDEX = 2
		val LAST_SIGNAL_INDEX = 5
		val activity = new ActivityComponent(0, 0, FIRST_SIGNAL_INDEX, LAST_SIGNAL_INDEX, true, null, null)

		val activityPoints = formatter.format(activity)

		activityPoints shouldBe ActivityPoints(0, 0, new ReferencePoint(Actor.bottomMiddle(0))
			.atY(max(Reference1DPoint(ViewMatrix.row(FIRST_SIGNAL_INDEX - 1)),
				new ReferencePoint(Activity.bottomLeft(0, -1)).down(1).y()))
			.left(1), 2, Reference1DPoint(ViewMatrix.row(LAST_SIGNAL_INDEX)))
	}
}
