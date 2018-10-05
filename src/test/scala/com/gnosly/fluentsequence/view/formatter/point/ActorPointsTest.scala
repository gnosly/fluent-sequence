package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.PointMap
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActorPointsTest extends FunSuite with Matchers {
  val ACTOR_ID = 0
  val TOP_LEFT = Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0))

  test("points") {
    val actorPoints = ActorPoints(ACTOR_ID, TOP_LEFT, Box(5, 5))

    actorPoints.toPoints(new PointMap) shouldBe
      Actor.topLeft(ACTOR_ID) -> TOP_LEFT.resolve(null) ::
        Actor.topRight(ACTOR_ID) -> Fixed2dPoint(5L, 0L) ::
        Actor.bottomMiddle(ACTOR_ID) -> Fixed2dPoint(2L, 5L) :: Nil
  }

}
