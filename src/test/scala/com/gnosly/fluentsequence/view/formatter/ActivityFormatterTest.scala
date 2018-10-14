package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTIVITY_FIXED_WIDTH
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.point._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActivityFormatterTest extends FunSuite with Matchers {
  val ACTOR_ID = 0
  val ACTIVITY_ID = 0
  val NOT_IMPORTANT = 0

  val formatter = new ActivityFormatter(new FixedPreRenderer)

  test("activity that begins from the start of the sequence") {
    val START_FROM_TOP = 0
    val LAST_SIGNAL_INDEX = 3
    val ACTOR_MIDDLE_BOTTOM_X = 15
    val ACTOR_MIDDLE_BOTTOM_Y = 10
    val ROW_OF_LAST_INDEX = 20

    val activity = ActivityModel(ACTIVITY_ID, ACTOR_ID, START_FROM_TOP, LAST_SIGNAL_INDEX)

    val activityPoints = formatter
      .format(activity)
      .toPoints(ResolvedPoints(Map(
        Actor.bottomMiddle(ACTOR_ID) -> Fixed2dPoint(ACTOR_MIDDLE_BOTTOM_X, ACTOR_MIDDLE_BOTTOM_Y),
        ViewMatrix.row(LAST_SIGNAL_INDEX) -> Fixed2dPoint(ROW_OF_LAST_INDEX, 0)
      )))

    val expectedTopLeft = Fixed2dPoint(ACTOR_MIDDLE_BOTTOM_X - ACTIVITY_FIXED_WIDTH / 2, ACTOR_MIDDLE_BOTTOM_Y)
    val expectedTopRight = expectedTopLeft.right(ACTIVITY_FIXED_WIDTH)
    val expectedBottomLeft = expectedTopLeft.atY(ROW_OF_LAST_INDEX)

    activityPoints shouldBe List(
      Activity.topLeft(ACTOR_ID, ACTIVITY_ID) -> expectedTopLeft,
      Activity.topRight(ACTOR_ID, ACTIVITY_ID) -> expectedTopRight,
      Activity.bottomLeft(ACTOR_ID, ACTIVITY_ID) -> expectedBottomLeft,
    )
  }

  test("activity that begins in the middle of the sequence") {
    val FIRST_SIGNAL_INDEX = 5
    val LAST_SIGNAL_INDEX = 10
    val ACTOR_MIDDLE_BOTTOM_X = 15
    val ROW_LAST_SIGNAL_BEFORE_ACTIVITY = 6
    val ROW_OF_LAST_INDEX = 20

    val activity = ActivityModel(ACTIVITY_ID, ACTOR_ID, FIRST_SIGNAL_INDEX, LAST_SIGNAL_INDEX)

    val activityPoints = formatter
      .format(activity)
      .toPoints(ResolvedPoints(Map(
        Actor.bottomMiddle(ACTOR_ID) -> Fixed2dPoint(ACTOR_MIDDLE_BOTTOM_X, NOT_IMPORTANT),
        ViewMatrix.row(FIRST_SIGNAL_INDEX - 1) -> Fixed2dPoint(ROW_LAST_SIGNAL_BEFORE_ACTIVITY, NOT_IMPORTANT),
        ViewMatrix.row(LAST_SIGNAL_INDEX) -> Fixed2dPoint(ROW_OF_LAST_INDEX, NOT_IMPORTANT)
      )))

    val expectedTopLeft =
      Fixed2dPoint(ACTOR_MIDDLE_BOTTOM_X - ACTIVITY_FIXED_WIDTH / 2, ROW_LAST_SIGNAL_BEFORE_ACTIVITY)
    val expectedTopRight = expectedTopLeft.right(ACTIVITY_FIXED_WIDTH)
    val expectedBottomLeft = expectedTopLeft.atY(ROW_OF_LAST_INDEX)

    activityPoints shouldBe List(
      Activity.topLeft(ACTOR_ID, ACTIVITY_ID) -> expectedTopLeft,
      Activity.topRight(ACTOR_ID, ACTIVITY_ID) -> expectedTopRight,
      Activity.bottomLeft(ACTOR_ID, ACTIVITY_ID) -> expectedBottomLeft,
    )
  }
}
