package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, ReferencePoint, Variable1DPoint}
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable

class MatrixFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"

  test("column when only one actor is defined") {
    val formatter = new MatrixFormatter(new FixedPreRenderer)
    val actor = new ActorComponent(0, ACTOR_NAME, isLast = true)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint((ACTOR_PADDING + ACTOR_NAME.length) / 2))
  }

  test("column where actor is not the last") {
    val formatter = new MatrixFormatter(new FixedPreRenderer)
    val actor = new ActorComponent(0, ACTOR_NAME, isLast = false)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS))
  }


	test("column where actor has got different signals") {
		val formatter = new MatrixFormatter(new FixedPreRenderer)

		val call = new SyncRequest("call", 0, 0, 0, 1, 0)
		val response = new SyncResponse("response", 1, 1, 0, 0, 0)

		val userRightPoints = mutable.TreeMap[Int, RightPoint](
			0 -> ActivityPointForBiSignalOnTheRight(0, call),
			1 -> ActivityPointForBiSignalOnTheRight(1, response)
		)

		val actor = new ActorComponent(0, ACTOR_NAME, asBuffer(new ActivityComponent(0, 0, 0, 0, rightPoints = userRightPoints)), isLast = false)

		val matrixPoint = formatter.format(actor)

		val actorStartX = new ReferencePoint(Coordinates.Actor.topLeft(0)).x
		val signalStartX = new ReferencePoint(Activity.pointStart(0, 0, 0, "right")).x
		val columnWidthForcedBySignal = signalStartX - actorStartX + Fixed1DPoint("call".length + FixedPreRenderer.BISIGNAL_FIXED_PADDING)

		matrixPoint shouldBe MatrixPoint(
			Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS) max columnWidthForcedBySignal
		)
	}

	private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
		mutable.Buffer[ActivityComponent](component)
	}
}
