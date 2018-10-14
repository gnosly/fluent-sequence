package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.AUTO_SIGNAL_MIN_HEIGHT
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.point._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class AutoSignalFormatterTest extends FunSuite with Matchers {
  val ARROW_WIDTH = 6
  val SIGNAL_NAME = "does"
  val SIGNAL_ID = 0
  val ACTOR_ID = 0
  val ACTIVITY_ID = 0

  val fixedWidthAutoSignalFormatter = new AutoSignalFormatter(new FixedPreRenderer)

  test("first autoSignal") {
    val ACTIVITY_TOP_X = 15
    val ACTIVITY_TOP_Y = 10

    val autoSignal = AutoSignalModel(SIGNAL_NAME, SIGNAL_ID, ACTOR_ID, ACTIVITY_ID)

    val points = fixedWidthAutoSignalFormatter
      .format(autoSignal)
      .toPoints(
        ResolvedPoints(
          Map(
            Activity.topRight(ACTOR_ID, ACTIVITY_ID) -> Fixed2dPoint(ACTIVITY_TOP_X, ACTIVITY_TOP_Y)
          )))

    val expectedRightPointStart = Fixed2dPoint(ACTIVITY_TOP_X + 1, ACTIVITY_TOP_Y + DISTANCE_BETWEEN_SIGNALS)
    val expectedRightPointEnd = expectedRightPointStart.down(AUTO_SIGNAL_MIN_HEIGHT)

    points shouldBe List(
      Activity.rightPointStart(ACTOR_ID, ACTIVITY_ID, SIGNAL_ID) -> expectedRightPointStart,
      Activity.rightPointEnd(ACTOR_ID, ACTIVITY_ID, SIGNAL_ID) -> expectedRightPointEnd
    )
  }

  test("second autoSignal") {
    val AUTOSIGNAL_INDEX = 1
    val ACTIVITY_TOP_X = 15
    val ROW_HEIGHT_PREVIOUS_SIGNAL = 6
    val NOT_IMPORTANT = -1

    val autoSignal = AutoSignalModel(SIGNAL_NAME, AUTOSIGNAL_INDEX, ACTOR_ID, ACTIVITY_ID)

    val points = fixedWidthAutoSignalFormatter
      .format(autoSignal)
      .toPoints(ResolvedPoints(Map(
        Activity.topRight(ACTOR_ID, ACTIVITY_ID) -> Fixed2dPoint(ACTIVITY_TOP_X, NOT_IMPORTANT),
        ViewMatrix.row(AUTOSIGNAL_INDEX - 1) -> Fixed2dPoint(ROW_HEIGHT_PREVIOUS_SIGNAL, NOT_IMPORTANT),
      )))

    val expectedRightPointStart =
      Fixed2dPoint(ACTIVITY_TOP_X + 1, ROW_HEIGHT_PREVIOUS_SIGNAL + DISTANCE_BETWEEN_SIGNALS)
    val expectedRightPointEnd = expectedRightPointStart.down(AUTO_SIGNAL_MIN_HEIGHT)

    points shouldBe List(
      Activity.rightPointStart(ACTOR_ID, ACTIVITY_ID, AUTOSIGNAL_INDEX) -> expectedRightPointStart,
      Activity.rightPointEnd(ACTOR_ID, ACTIVITY_ID, AUTOSIGNAL_INDEX) -> expectedRightPointEnd
    )
  }
}
