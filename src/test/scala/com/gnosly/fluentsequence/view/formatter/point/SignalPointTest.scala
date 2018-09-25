package com.gnosly.fluentsequence.view.formatter.point

import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, Fixed2dPoint, PointMap, Variable2DPoint}
import com.gnosly.fluentsequence.view.model.{Box, Coordinates}
import org.scalatest.{FunSuite, Matchers}

class SignalPointTest extends FunSuite with Matchers {
  private val SIGNAL_WIDTH = 10
  private val SIGNAL_HEIGHT = 3

  test("constraint with a right side signal") {
    val signalPoint =
      SignalPoint(0, 0, 0, Box(SIGNAL_WIDTH, SIGNAL_HEIGHT), "right", Variable2DPoint(Fixed1DPoint(14L), Fixed1DPoint(0L)))

    val map = new PointMap
    map.putAll(Coordinates.Actor.topLeft(0) -> Fixed2dPoint(10L, 0L) :: Nil)

    signalPoint.toMatrixConstraints(map) shouldBe
      ViewMatrix.column(0) -> Fixed1DPoint(SIGNAL_WIDTH + 4) ::
        ViewMatrix.row(0) -> Fixed1DPoint(SIGNAL_HEIGHT) :: Nil
  }

  test("constraint with a left side signal") {
    val signalPoint =
      SignalPoint(0, 0, 0, Box(SIGNAL_WIDTH, SIGNAL_HEIGHT), "left", Variable2DPoint(Fixed1DPoint(0L), Fixed1DPoint(0L)))

    signalPoint.toMatrixConstraints(new PointMap) shouldBe
      ViewMatrix.column(0) -> Fixed1DPoint(0) ::
        ViewMatrix.row(0) -> Fixed1DPoint(SIGNAL_HEIGHT) :: Nil
  }
}
