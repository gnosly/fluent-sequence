package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.formatter.point.WidthAndHeightPoint
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModelFactory.viewModelFrom
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

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

    val outerWidth = new ReferencePoint(Coordinates.Actor.topLeft(LAST_ACTOR_ID))
      .right(new ReferencePoint(Coordinates.ViewMatrix.column(LAST_ACTOR_ID)).x)
      .right(FormatterConstants.RIGHT_MARGIN)
      .atY(0)

    val outerHeight = new ReferencePoint(Coordinates.ViewMatrix.row(LAST_SIGNAL))
      .right(FormatterConstants.BOTTOM_MARGIN)

    pointable shouldBe WidthAndHeightPoint(outerWidth, outerHeight) //FIXME it is down but I'm using 2dPoint for a distance
  }
}
