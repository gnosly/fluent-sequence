package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_MIN_HEIGHT
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.LEFT_MARGIN
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.TOP_MARGIN
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.point._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActorFormatterTest extends FunSuite with Matchers {
  val ACTOR_ID = 0
  val ACTOR_NAME = "user"
  val LAST_ACTOR = true

  val formatter = new ActorFormatter(new FixedPreRenderer)

  test("format first actor alone") {

    val points = formatter
      .format(ActorModel(ACTOR_ID, ACTOR_NAME, LAST_ACTOR))
      .toPoints(ResolvedPoints(Map()))

    val expectedTopLeft = Fixed2dPoint(LEFT_MARGIN, TOP_MARGIN)
    val expectedTopRight = expectedTopLeft.right(ACTOR_NAME.length + ACTOR_PADDING)
    val expectedBottomMiddle = expectedTopLeft.right((ACTOR_NAME.length + ACTOR_PADDING - 1) / 2).down(ACTOR_MIN_HEIGHT)

    points shouldBe List(
      Actor.topLeft(ACTOR_ID) -> expectedTopLeft,
      Actor.topRight(ACTOR_ID) -> expectedTopRight,
      Actor.bottomMiddle(ACTOR_ID) -> expectedBottomMiddle
    )
  }

  test("format second actor when before is nearer than DISTANCE_BETWEEN_ACTORS") {
    val ACTOR_ID = 1
    val PREVIOUS_ACTOR_ID = 0
    val PREVIOUS_ACTOR_TOP_LEFT_X = 5
    val PREVIOUS_COLUMN_WIDTH = DISTANCE_BETWEEN_ACTORS - 3
    val NOT_IMPORTANT = 0

    val points = formatter
      .format(ActorModel(ACTOR_ID, ACTOR_NAME, LAST_ACTOR))
      .toPoints(ResolvedPoints(Map(
        Actor.topLeft(PREVIOUS_ACTOR_ID) -> Fixed2dPoint(PREVIOUS_ACTOR_TOP_LEFT_X, TOP_MARGIN),
        ViewMatrix.column(PREVIOUS_ACTOR_ID) -> Fixed2dPoint(PREVIOUS_COLUMN_WIDTH, NOT_IMPORTANT)
      )))

    val expectedTopLeft = Fixed2dPoint(PREVIOUS_ACTOR_TOP_LEFT_X + DISTANCE_BETWEEN_ACTORS, TOP_MARGIN)
    val expectedTopRight = expectedTopLeft.right(ACTOR_NAME.length + ACTOR_PADDING)
    val expectedBottomMiddle = expectedTopLeft.right((ACTOR_NAME.length + ACTOR_PADDING - 1) / 2).down(ACTOR_MIN_HEIGHT)

    points shouldBe List(
      Actor.topLeft(ACTOR_ID) -> expectedTopLeft,
      Actor.topRight(ACTOR_ID) -> expectedTopRight,
      Actor.bottomMiddle(ACTOR_ID) -> expectedBottomMiddle
    )
  }
}
