package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.formatter.point.WidthAndHeightPoint
import com.gnosly.fluentsequence.view.model.{Coordinates, ViewModelComponents}
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory.viewModelFrom
import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, ReferencePoint, Variable2DPoint}
import org.scalatest.{FunSuite, Matchers}

class WidthAndHeightFormatterTest extends FunSuite with Matchers {
  val SYSTEM = Actor(SEQUENCE_ACTOR_TYPE(), "system")
  val USER = Actor(USER_TYPE(), "user")

  val ACTOR_ID = 0

  val formatter = new WidthAndHeightFormatter

  test("multiple actor") {
    val LAST_ACTOR_ID = 1
    val LAST_SIGNAL = 1

    val pointable = formatter.format(
      viewModelFrom(
        EventBook(
          CALLED(USER, "call", SYSTEM),
          REPLIED(SYSTEM, "response", USER)
        )))

    pointable shouldBe
      WidthAndHeightPoint(
        new ReferencePoint(Coordinates.Actor.topLeft(LAST_ACTOR_ID)).right(new ReferencePoint(Coordinates.ViewMatrix.column(LAST_ACTOR_ID)).x).atY(0),
        new ReferencePoint(Coordinates.ViewMatrix.row(LAST_SIGNAL))
      )
  }
}
