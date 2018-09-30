package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.AUTO_SIGNAL_FIXED_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable

class MatrixFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"
  val SIGNAL_NAME = "SIGNAL NAME"
  val ACTOR_ID = 0
  val ANOTHER_ACTOR_ID = 1
  val ACTIVITY_ID = 0
  val NOT_IMPORTANT = 0
  val FIRST_INDEX = 0

  val formatter = new MatrixFormatter(new FixedPreRenderer)

  test("column when only one actor is defined") {

    val actor = new ActorComponent(ACTOR_ID, ACTOR_NAME, isLast = true)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint((ACTOR_PADDING + ACTOR_NAME.length) / 2))
  }

  test("column where actor is not the last") {

    val actor = new ActorComponent(ACTOR_ID, ACTOR_NAME, isLast = false)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS))
  }

  test("column where actor has got different signals") {

    val call = new SyncRequest(SIGNAL_NAME, FIRST_INDEX, ACTOR_ID, ACTIVITY_ID, ANOTHER_ACTOR_ID, NOT_IMPORTANT)

    val matrixPoint = formatter.format(actorWith(ActivityPointForBiSignalOnTheRight(FIRST_INDEX, call)))

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(ACTOR_ID)).x
    val signalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, FIRST_INDEX, "right")).x
    val signalWidth = Fixed1DPoint(SIGNAL_NAME.length + FixedPreRenderer.BISIGNAL_FIXED_PADDING)
    val columnWidthForcedBySignal = signalStartX - actorStartX + signalWidth

    matrixPoint shouldBe MatrixPoint(
      Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS) max columnWidthForcedBySignal
    )
  }

  test("column where actor has got auto signals") {

    val somethingSignal = new AutoSignalComponent(SIGNAL_NAME, ACTOR_ID, ACTOR_ID, ACTIVITY_ID)

    val matrixPoint = formatter.format(actorWith(ActivityPointLoopOnTheRight(FIRST_INDEX, somethingSignal)))

    val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(ACTOR_ID)).x
    val signalStartX = new ReferencePoint(Activity.pointStart(ACTOR_ID, ACTIVITY_ID, FIRST_INDEX, "right")).x
    val signalWidth = Fixed1DPoint(SIGNAL_NAME.length + AUTO_SIGNAL_FIXED_PADDING)
    val columnWidthForcedBySignal = signalStartX - actorStartX + signalWidth

    matrixPoint shouldBe MatrixPoint(
      Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS) max columnWidthForcedBySignal
    )
  }

  private def actorWith(rightPoint: RightPoint) = {
    val userRightPoints = mutable.TreeMap[Int, RightPoint](
      rightPoint.signalComponent.currentIndex -> rightPoint,
    )

    val actor = new ActorComponent(
      ACTOR_ID,
      ACTOR_NAME,
      asBuffer(
        new ActivityComponent(ACTIVITY_ID, ACTOR_ID, NOT_IMPORTANT, NOT_IMPORTANT, rightPoints = userRightPoints)),
      isLast = false)
    actor
  }

  private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
    mutable.Buffer[ActivityComponent](component)
  }

}
