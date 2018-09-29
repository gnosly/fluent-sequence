package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.LEFT_MARGIN
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.TOP_MARGIN
import com.gnosly.fluentsequence.view.formatter.point.ActorPoints
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.Reference1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActorFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"
  val ACTOR_PADDING = 4
  val formatter = new ActorFormatter(new FixedPreRenderer)

  test("format first actor alone") {
    formatter.format(new ActorComponent(0, ACTOR_NAME, null)) shouldBe
      ActorPoints(0, new Variable2DPoint(LEFT_MARGIN, TOP_MARGIN), Box(ACTOR_NAME.length + ACTOR_PADDING, 4))
  }

  test("format second actor alone") {
    val ACTOR_ID = 1
    val PREVIOUS_ACTOR_ID = 0

    formatter.format(new ActorComponent(ACTOR_ID, ACTOR_NAME, null)) shouldBe
      ActorPoints(
        ACTOR_ID,
        new ReferencePoint(Actor.topLeft(PREVIOUS_ACTOR_ID))
          .right(Reference1DPoint(s"column_${PREVIOUS_ACTOR_ID}") max Fixed1DPoint(DISTANCE_BETWEEN_ACTORS)),
        Box(ACTOR_NAME.length + ACTOR_PADDING, 4)
      )
  }

}
