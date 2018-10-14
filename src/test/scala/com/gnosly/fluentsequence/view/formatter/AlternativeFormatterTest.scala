package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Coordinates.Alternative
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class AlternativeFormatterTest extends FunSuite with Matchers {
  val USER = Actor(USER_TYPE(), "user")
  val CURRENT_ACTIVITY = 0
  val LAST_INDEX_BEFORE_ALTERNATIVE = 0
  val LAST_INDEX_INSIDE_ALTERNATIVE = 1
  val ALTERNATIVE_ID = 0

  val formatter = new AlternativeFormatter

  test("alternative at start") {}

  test("alternative in the middle") {
    val alternative =
      AlternativeComponent(ALTERNATIVE_ID, "", LAST_INDEX_BEFORE_ALTERNATIVE, LAST_INDEX_INSIDE_ALTERNATIVE)

    val points = formatter
      .format(alternative)
      .toPoints(ResolvedPoints(Map(
        ViewMatrix.row(LAST_INDEX_BEFORE_ALTERNATIVE) -> Fixed2dPoint(10, 0),
        ViewMatrix.row(LAST_INDEX_INSIDE_ALTERNATIVE) -> Fixed2dPoint(20, 0),
        ViewMatrix.width() -> Fixed2dPoint(50, 0),
      )))

    points shouldBe List(
      Alternative.topLeft(ALTERNATIVE_ID) -> Fixed2dPoint(1, 10),
      Alternative.bottomRight(ALTERNATIVE_ID) -> Fixed2dPoint(50 - 2, 20)
    )
  }
}
