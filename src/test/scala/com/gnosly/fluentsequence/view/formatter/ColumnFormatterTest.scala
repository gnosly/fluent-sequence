package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.AUTO_SIGNAL_FIXED_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.point.ColumnPoint
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable

class ColumnFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"
  val SIGNAL_NAME = "SIGNAL NAME"
  val ACTOR_ID = 0
  val ANOTHER_ACTOR_ID = 1
  val ACTIVITY_ID = 0
  val NOT_IMPORTANT = 0
  val FIRST_INDEX = 0

  val formatter = new ColumnFormatter(new FixedPreRenderer)

  test("column when only one actor is defined") {

    val actor = new ActorComponent(ACTOR_ID, ACTOR_NAME, isLast = true)

    formatter.format(actor) shouldBe ColumnPoint(ACTOR_ID, Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length))
  }

  test("column where actor is not the last") {

    val actor = new ActorComponent(ACTOR_ID, ACTOR_NAME, isLast = false)

    formatter.format(actor) shouldBe ColumnPoint(
      ACTOR_ID,
      Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS))
  }

  test("column where actor has got different signals") {

    val call = new SyncRequest(SIGNAL_NAME, FIRST_INDEX, ACTOR_ID, ACTIVITY_ID, ANOTHER_ACTOR_ID, NOT_IMPORTANT)

    val columnPoint = formatter.format(actorWith(PointOnTheRight(FIRST_INDEX, call), false))

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(ACTOR_ID)).x
    val signalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, FIRST_INDEX, "right")).x
    val signalWidth = Fixed1DPoint(SIGNAL_NAME.length + FixedPreRenderer.BISIGNAL_FIXED_PADDING)
    val columnWidthForcedBySignal = signalStartX - actorStartX + signalWidth

    columnPoint shouldBe ColumnPoint(
      ACTOR_ID,
      Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS) max columnWidthForcedBySignal)
  }

  test("column where actor has got replied signal more length then called signal") {

    val call = new SyncRequest(SIGNAL_NAME, FIRST_INDEX, ACTOR_ID, ACTIVITY_ID, ANOTHER_ACTOR_ID, NOT_IMPORTANT)
    val reply = new SyncResponse("VERY LONG MESSAGE", 1, ANOTHER_ACTOR_ID, ACTIVITY_ID, ACTOR_ID, NOT_IMPORTANT)

    val userRightPoints = mutable.ListBuffer[PointOnTheRight](
      PointOnTheRight(FIRST_INDEX, call),
      PointOnTheRight(1, reply)
    )

    val actor = new ActorComponent(
      ACTOR_ID,
      ACTOR_NAME,
      asBuffer(
        new ActivityComponent(ACTIVITY_ID, ACTOR_ID, NOT_IMPORTANT, NOT_IMPORTANT, _rightPoints = userRightPoints)))

    val columnPoint = formatter.format(actor)

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(ACTOR_ID)).x
    val signalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, FIRST_INDEX, "right")).x
    val signalWidth = Fixed1DPoint(SIGNAL_NAME.length + FixedPreRenderer.BISIGNAL_FIXED_PADDING)
    val columnWidthForcedByCallSignal = signalStartX - actorStartX + signalWidth

    val replySignalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, 1, "right")).x
    val replySignalWidth = Fixed1DPoint("VERY LONG MESSAGE".length + FixedPreRenderer.BISIGNAL_FIXED_PADDING)
    val columnWidthForcedByReplySignal = replySignalStartX - actorStartX + replySignalWidth

    columnPoint shouldBe ColumnPoint(
      ACTOR_ID,
      Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS) max columnWidthForcedByCallSignal max columnWidthForcedByReplySignal)
  }

  test("column where actor has got auto signals") {

    val somethingSignal = new AutoSignalComponent(SIGNAL_NAME, ACTOR_ID, ACTOR_ID, ACTIVITY_ID)

    val columnPoint = formatter.format(actorWith(PointOnTheRight(FIRST_INDEX, somethingSignal), false))

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(ACTOR_ID)).x
    val signalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, FIRST_INDEX, "right")).x
    val signalWidth = Fixed1DPoint(SIGNAL_NAME.length + AUTO_SIGNAL_FIXED_PADDING)
    val columnWidthForcedBySignal = signalStartX - actorStartX + signalWidth

    columnPoint shouldBe ColumnPoint(
      ACTOR_ID,
      Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS) max columnWidthForcedBySignal)
  }

  test("column where actor has got auto signals as last") {

    val somethingSignal = new AutoSignalComponent(SIGNAL_NAME, ACTOR_ID, ACTOR_ID, ACTIVITY_ID)

    val columnPoint = formatter.format(actorWith(PointOnTheRight(FIRST_INDEX, somethingSignal), true))

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(ACTOR_ID)).x
    val signalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, FIRST_INDEX, "right")).x
    val signalWidth = Fixed1DPoint(SIGNAL_NAME.length + AUTO_SIGNAL_FIXED_PADDING)
    val columnWidthForcedBySignal = signalStartX - actorStartX + signalWidth

    columnPoint shouldBe ColumnPoint(ACTOR_ID,
                                     Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length) max columnWidthForcedBySignal)
  }

  private def actorWith(rightPoint: PointOnTheRight, last: Boolean) = {
    val userRightPoints = mutable.ListBuffer(rightPoint)
    val actor = new ActorComponent(
      ACTOR_ID,
      ACTOR_NAME,
      asBuffer(
        new ActivityComponent(ACTIVITY_ID, ACTOR_ID, NOT_IMPORTANT, NOT_IMPORTANT, _rightPoints = userRightPoints)),
      isLast = last)
    actor
  }

  private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
    mutable.Buffer[ActivityComponent](component)
  }

}
