package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.formatter.point.SignalPoint
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates.{Activity, ViewMatrix}
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, Reference1DPoint, ReferencePoint, Variable2DPoint}
import org.scalatest.{FunSuite, Matchers}

class AutoSignalFormatterTest extends FunSuite with Matchers {
  val ARROW_WIDTH = 6
  val SIGNAL_NAME = "does"

  val fixedWidthAutoSignalFormatter = new AutoSignalFormatter(new FixedPreRenderer())

  test("first autoSignal") {
    val autoSignal = new AutoSignalComponent(SIGNAL_NAME, 0, 0, 0)

    val point = fixedWidthAutoSignalFormatter.format(autoSignal)

    point shouldBe new SignalPoint(0,
                                   0,
                                   0,
                                   Box(ARROW_WIDTH + SIGNAL_NAME.length, 4),
                                   "right",
                                   new ReferencePoint(Activity.topRight(0, 0)).down(1).right(1))
  }

  test("second autoSignal") {
    val AUTOSIGNAL_INDEX = 1
    val autoSignal = new AutoSignalComponent(SIGNAL_NAME, AUTOSIGNAL_INDEX, 0, 0)

    val point = fixedWidthAutoSignalFormatter.format(autoSignal)

    point shouldBe new SignalPoint(
      0,
      0,
      AUTOSIGNAL_INDEX,
      Box(ARROW_WIDTH + SIGNAL_NAME.length, 4),
      "right",
      Variable2DPoint(new ReferencePoint(Activity.topRight(0, 0)).right(1).x(),
                      Reference1DPoint(ViewMatrix.row(AUTOSIGNAL_INDEX - 1)) + Fixed1DPoint(DISTANCE_BETWEEN_SIGNALS))
    )
  }
}
