package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.formatter.FormatterConstants._
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.PointMap
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActorPointsTest extends FunSuite with Matchers {
  val BOX_WIDTH = 12

  test("points") {
    val actorPoints = ActorPoints(0, Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0)), Box(5, 5))
    actorPoints.toPoints(new PointMap) shouldBe
      Actor.topLeft(0) -> Fixed2dPoint(0L, 0L) ::
        Actor.topRight(0) -> Fixed2dPoint(5L, 0L) ::
        Actor.bottomMiddle(0) -> Fixed2dPoint(2L, 5L) :: Nil
  }

  test("constraint one actor") {
    val actorPoints = ActorPoints(0, Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0)), Box(BOX_WIDTH, 5))
    actorPoints.toMatrixConstraints(new PointMap) shouldBe Nil
  }

  test("constraint two actors") {
    val actorPoints = ActorPoints(0, Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0)), Box(BOX_WIDTH, 5))
    val map = new PointMap
    map.putAll(Actor.topLeft(1) -> Fixed2dPoint(1, 2) :: Nil)

    actorPoints.toMatrixConstraints(map) shouldBe Nil
  }
}
