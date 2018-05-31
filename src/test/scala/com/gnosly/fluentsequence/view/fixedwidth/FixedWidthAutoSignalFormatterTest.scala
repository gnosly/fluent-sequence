package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.{Activity, SignalPoint, ViewMatrix}
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.model.AutoSignalComponent
import org.scalatest.{FunSuite, Matchers}

class FixedWidthAutoSignalFormatterTest extends FunSuite with Matchers {
	val fixedWidthAutoSignalFormatter = new FixedWidthAutoSignalFormatter(new FixedWidthPainter())

	test("first autoSignal") {
		val autoSignal = new AutoSignalComponent("does", 1, 0, 0)

		val point = fixedWidthAutoSignalFormatter.format(autoSignal)

		point shouldBe new SignalPoint(0, 0, 1, Box(10, 4), "right",
			new ReferencePoint(Activity.topRight(0, 0)).down(1).right(1))
	}


	test("second autoSignal") {
		val AUTOSIGNAL_INDEX = 2
		val autoSignal = new AutoSignalComponent("does", AUTOSIGNAL_INDEX, 0, 0)

		val point = fixedWidthAutoSignalFormatter.format(autoSignal)

		point shouldBe new SignalPoint(0, 0, AUTOSIGNAL_INDEX, Box(10, 4), "right",
			Fixed2DPoint(new ReferencePoint(Activity.topRight(0, 0)).right(1).x(),
				Reference1DPoint(ViewMatrix.row(AUTOSIGNAL_INDEX - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS)))
	}

}
