package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.LEFT_MARGIN
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.TOP_MARGIN
import com.gnosly.fluentsequence.view.formatter.point.ActorPoints
import com.gnosly.fluentsequence.view.model.ActorModel
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.Reference1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActorFormatterTest extends FunSuite with Matchers {
  val ACTOR_ID = 0
  val ACTOR_NAME = "user"
  val formatter = new ActorFormatter(new FixedPreRenderer)

  test("format first actor alone") {

    formatter.format(ActorModel(ACTOR_ID, ACTOR_NAME)) shouldBe
      ActorPoints(ACTOR_ID,
                  new Variable2DPoint(LEFT_MARGIN, TOP_MARGIN),
                  Box(ACTOR_NAME.length + ACTOR_PADDING, ACTOR_PADDING))
  }

  test("format second actor alone") {
    val ACTOR_ID = 1
    val PREVIOUS_ACTOR_ID = 0

    formatter.format(ActorModel(ACTOR_ID, ACTOR_NAME)) shouldBe
      ActorPoints(
        ACTOR_ID,
        new ReferencePoint(Actor.topLeft(PREVIOUS_ACTOR_ID))
          .right(Reference1DPoint(ViewMatrix.column(PREVIOUS_ACTOR_ID)) max Fixed1DPoint(DISTANCE_BETWEEN_ACTORS)),
        Box(ACTOR_NAME.length + ACTOR_PADDING, ACTOR_PADDING)
      )
  }

}
