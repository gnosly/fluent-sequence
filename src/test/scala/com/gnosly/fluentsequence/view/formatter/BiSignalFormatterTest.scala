package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.point.SignalPoint
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.{FunSuite, Matchers}

class BiSignalFormatterTest extends FunSuite with Matchers {
  val SIGNAL_NAME = "signalName"
  val BISIGNAL_PADDING = 5

  test("first bisignal on the right") {
    val signal = new BiSignalComponent(SIGNAL_NAME, 0, 0, 0, 1, 0)

    val point = new BiSignalFormatter(new FixedPreRenderer()).formatOnRight(signal)

    point shouldBe new SignalPoint(0,
                                   0,
                                   0,
                                   Box(BISIGNAL_PADDING + SIGNAL_NAME.length, 2),
                                   "right",
                                   new ReferencePoint(Activity.topRight(0, 0)).down(1).right(1))
  }
}
