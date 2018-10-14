package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.BISIGNAL_MIN_HEIGHT
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_SIGNALS
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.SyncRequest
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

class BiSignalFormatterTest extends FunSuite with Matchers {
  val NOT_IMPORTANT = ""
  val NOT_IMPORTANT_INT = -1
  val SIGNAL_INDEX = 0
  val ACTOR_ID = 0
  val ACTIVITY_ID = 0
  val ROW_HEIGHT_AT_PREVIOUS_INDEX = 14

  val formatter = new BiSignalFormatter(new FixedPreRenderer)

  test("bisignal on the right") {
    val signal =
      new SyncRequest(NOT_IMPORTANT, SIGNAL_INDEX, ACTOR_ID, ACTIVITY_ID, NOT_IMPORTANT_INT, NOT_IMPORTANT_INT)

    val points = formatter
      .formatOnRight(signal)
      .toPoints(
        ResolvedPoints(
          Map(
            ViewMatrix.row(SIGNAL_INDEX - 1) -> Fixed2dPoint(ROW_HEIGHT_AT_PREVIOUS_INDEX, 0)
          )))

    val expectedPointStart = Fixed2dPoint(0, ROW_HEIGHT_AT_PREVIOUS_INDEX + DISTANCE_BETWEEN_SIGNALS)
    val expectedPointEnd = expectedPointStart.down(BISIGNAL_MIN_HEIGHT)

    points contains (
      Activity.pointStart(ACTOR_ID, ACTIVITY_ID, SIGNAL_INDEX, "right") -> expectedPointStart,
      Activity.pointEnd(ACTOR_ID, ACTIVITY_ID, SIGNAL_INDEX, "right") -> expectedPointEnd
    )
  }
}
