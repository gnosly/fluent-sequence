package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.formatter.point.AlternativePoints
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class AlternativeFormatterTest extends FunSuite with Matchers {
  val USER = Actor(USER_TYPE(), "user")
  val FIRST_ACTOR_ID = 0
  val CURRENT_ACTIVITY = 0
  val LAST_INDEX_BEFORE_ALTERNATIVE = 0
  val LAST_INDEX_INSIDE_ALTERNATIVE = 1
  val ALTERNATIVE_ID = 0

  val formatter = new AlternativeFormatter

  test("alternative at start") {}

  test("alternative in the middle") {
    val alternative =
      AlternativeComponent(ALTERNATIVE_ID, "", LAST_INDEX_BEFORE_ALTERNATIVE, LAST_INDEX_INSIDE_ALTERNATIVE)

    val pointable = formatter.format(alternative)

    pointable shouldBe AlternativePoints(
      ALTERNATIVE_ID,
      new ReferencePoint(ViewMatrix.column(FIRST_ACTOR_ID))
        .atY(new ReferencePoint(ViewMatrix.row(LAST_INDEX_BEFORE_ALTERNATIVE)).x)
    )
  }
}
