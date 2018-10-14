package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTIVITY_FIXED_WIDTH
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.AUTO_SIGNAL_FIXED_PADDING
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.BISIGNAL_FIXED_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.point.ColumnPoint
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.ViewModels._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ColumnFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"
  val SIGNAL_NAME = "SIGNAL NAME"
  val VERY_LONG_MESSAGE = "VERY LONG MESSAGE"
  val ACTOR_ID = 0
  val ANOTHER_ACTOR_ID = 1
  val ACTIVITY_ID = 0
  val NOT_IMPORTANT = 0
  val FIRST_INDEX = 0
  val NOT_RIGHT_POINTS = List()

  val formatter = new ColumnFormatter(new FixedPreRenderer)

  test("column with the last actor") {

    val actor = ActorModel(ACTOR_ID, ACTOR_NAME, isLast = true)

    formatter.format(actor, NOT_RIGHT_POINTS) shouldBe ColumnPoint(ACTOR_ID, ACTOR_PADDING + ACTOR_NAME.length)
  }

  test("column where actor is not the last") {

    val actor = ActorModel(ACTOR_ID, ACTOR_NAME, isLast = false)

    formatter.format(actor, NOT_RIGHT_POINTS) shouldBe ColumnPoint(
      ACTOR_ID,
      ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS)
  }

  test("column where actor has got different signals") {

    val call = new SyncRequest(SIGNAL_NAME, FIRST_INDEX, ACTOR_ID, ACTIVITY_ID, ANOTHER_ACTOR_ID, NOT_IMPORTANT)

    val actor = ActorModel(ACTOR_ID, ACTOR_NAME, isLast = false)

    val columnPoint = formatter.format(actor, PointOnTheRight(FIRST_INDEX, call) :: Nil)

    columnPoint shouldBe ColumnPoint(
      ACTOR_ID,
      (ACTOR_PADDING + ACTOR_NAME.length + ACTIVITY_FIXED_WIDTH) / 2 + SIGNAL_NAME.length + BISIGNAL_FIXED_PADDING)
  }

  test("column where actor has got replied signal more length then called signal") {

    val call = new SyncRequest(SIGNAL_NAME, FIRST_INDEX, ACTOR_ID, ACTIVITY_ID, ANOTHER_ACTOR_ID, NOT_IMPORTANT)
    val reply = new SyncResponse(VERY_LONG_MESSAGE, 1, ANOTHER_ACTOR_ID, ACTIVITY_ID, ACTOR_ID, NOT_IMPORTANT)

    val userRightPoints = List(
      PointOnTheRight(FIRST_INDEX, call),
      PointOnTheRight(1, reply)
    )

    val actor = ActorModel(ACTOR_ID, ACTOR_NAME, isLast = false)

    val columnPoint = formatter.format(actor, userRightPoints)

    columnPoint shouldBe ColumnPoint(
      ACTOR_ID,
      (ACTOR_PADDING + ACTOR_NAME.length + ACTIVITY_FIXED_WIDTH) / 2 + VERY_LONG_MESSAGE.length + BISIGNAL_FIXED_PADDING)
  }

  test("column where actor has got auto signals") {
    val somethingSignal = AutoSignalModel(SIGNAL_NAME, ACTOR_ID, ACTOR_ID, ACTIVITY_ID)

    val actor = ActorModel(ACTOR_ID, ACTOR_NAME, isLast = false)

    val columnPoint = formatter.format(actor, PointOnTheRight(FIRST_INDEX, somethingSignal) :: Nil)

    columnPoint shouldBe ColumnPoint(
      ACTOR_ID,
      (ACTOR_PADDING + ACTOR_NAME.length + ACTIVITY_FIXED_WIDTH) / 2 + SIGNAL_NAME.length + AUTO_SIGNAL_FIXED_PADDING)
  }
}
