package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTIVITY_FIXED_WIDTH
import com.gnosly.fluentsequence.view.formatter.point.ActivityPoints
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.model.point.Reference1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActivityFormatterTest extends FunSuite with Matchers {
  val ACTOR_ID = 0
  val ACTIVITY_ID = 0

  val formatter = new ActivityFormatter(new FixedPreRenderer)

  test("format first activity") {
    val LAST_SIGNAL_INDEX = 3

    val activity = new ActivityComponent(ACTIVITY_ID, ACTOR_ID, 0, LAST_SIGNAL_INDEX, true, null, null)

    val activityPoints = formatter.format(activity)

    activityPoints shouldBe ActivityPoints(ACTOR_ID,
                                           ACTIVITY_ID,
                                           new ReferencePoint(Actor.bottomMiddle(ACTOR_ID)).left(1),
                                           ACTIVITY_FIXED_WIDTH,
                                           Reference1DPoint(ViewMatrix.row(LAST_SIGNAL_INDEX)))
  }

  test("format second activity") {
    val FIRST_SIGNAL_INDEX = 2
    val LAST_SIGNAL_INDEX = 5
    val activity = new ActivityComponent(ACTIVITY_ID, ACTOR_ID, FIRST_SIGNAL_INDEX, LAST_SIGNAL_INDEX, true, null, null)

    val activityPoints = formatter.format(activity)

    activityPoints shouldBe ActivityPoints(
      ACTOR_ID,
      ACTIVITY_ID,
      new ReferencePoint(Actor.bottomMiddle(ACTOR_ID))
        .atY(Reference1DPoint(ViewMatrix.row(FIRST_SIGNAL_INDEX - 1)) max
          new ReferencePoint(Activity.bottomLeft(ACTOR_ID, -1)).down(1).y)
        .left(1),
			ACTIVITY_FIXED_WIDTH,
      Reference1DPoint(ViewMatrix.row(LAST_SIGNAL_INDEX))
    )
  }
}
