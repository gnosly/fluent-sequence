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
  val NOT_IMPORTANT = -1

  val fixedWidthAutoSignalFormatter = new AutoSignalFormatter(new FixedPreRenderer)

  test("autoSignal") {
    val AUTO_SIGNAL_INDEX = 1
    val ACTIVITY_TOP_X = 15
    val ROW_HEIGHT_PREVIOUS_SIGNAL = 6

    val autoSignal = AutoSignalModel(SIGNAL_NAME, AUTO_SIGNAL_INDEX, ACTOR_ID, ACTIVITY_ID)

    val points = fixedWidthAutoSignalFormatter
      .format(autoSignal)
      .toPoints(ResolvedPoints(Map(
        Activity.topRight(ACTOR_ID, ACTIVITY_ID) -> Fixed2dPoint(ACTIVITY_TOP_X, NOT_IMPORTANT),
        ViewMatrix.row(AUTO_SIGNAL_INDEX - 1) -> Fixed2dPoint(ROW_HEIGHT_PREVIOUS_SIGNAL, NOT_IMPORTANT),
      )))

    val expectedRightPointStart =
      Fixed2dPoint(ACTIVITY_TOP_X + 1, ROW_HEIGHT_PREVIOUS_SIGNAL + DISTANCE_BETWEEN_SIGNALS)
    val expectedRightPointEnd = expectedRightPointStart.down(AUTO_SIGNAL_MIN_HEIGHT)

    points shouldBe List(
      Activity.rightPointStart(ACTOR_ID, ACTIVITY_ID, AUTO_SIGNAL_INDEX) -> expectedRightPointStart,
      Activity.rightPointEnd(ACTOR_ID, ACTIVITY_ID, AUTO_SIGNAL_INDEX) -> expectedRightPointEnd
    )
  }
}
