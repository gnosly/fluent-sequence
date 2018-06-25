package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Coordinates.Activity
import com.gnosly.fluentsequence.view.fixedwidth.formatter.FixedWidthBiSignalFormatter
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.model.point.SignalPoint
import org.scalatest.{FunSuite, Matchers}

class FixedWidthBiSignalFormatterTest extends FunSuite with Matchers {
	val SIGNAL_NAME = "signalName"
	val BISIGNAL_PADDING = 5

	test("first bisignal on the right") {
		val signal = new BiSignalComponent(SIGNAL_NAME, 0, 0, 0, 1, 0)

		val point = new FixedWidthBiSignalFormatter(new FixedWidthPreRenderer()).formatOnRight(signal)

		point shouldBe new SignalPoint(0, 0, 0, Box(BISIGNAL_PADDING + SIGNAL_NAME.length, 2), "right",
			new ReferencePoint(Activity.topRight(0, 0)).down(1).right(1))
	}
}
