package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.ALTERNATIVE_MIN_HEIGHT
import com.gnosly.fluentsequence.view.model.AlternativeBuilder
import com.gnosly.fluentsequence.view.model.Coordinates.Alternative
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.AlternativeModel
import com.gnosly.fluentsequence.view.model.point._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class AlternativeFormatterTest extends FunSuite with Matchers {
  val USER = Actor(USER_TYPE(), "user")
  val CURRENT_ACTIVITY = 0
  val ALTERNATIVE_START_INDEX = 0
  val LAST_INDEX_INSIDE_ALTERNATIVE = 1
  val ID = 0

  val formatter = new AlternativeFormatter

  test("alternative at start") {}

  test("alternative in the middle") {
    val ROW_HEIGHT_BEFORE_ALTERNATIVE = 10
    val ROW_HEIGHT_AT_ALTERNATIVE_END = 20
    val NOT_IMPORTANT = 0
    val SEQUENCE_DIAGRAM_WIDTH = 50

    val alternative = AlternativeModel(ID, "", ALTERNATIVE_START_INDEX, LAST_INDEX_INSIDE_ALTERNATIVE)

    val points = formatter
      .format(alternative)
      .toPoints(ResolvedPoints(Map(
        ViewMatrix.row(ALTERNATIVE_START_INDEX - 1) -> Fixed2dPoint(ROW_HEIGHT_BEFORE_ALTERNATIVE, NOT_IMPORTANT),
        ViewMatrix.row(LAST_INDEX_INSIDE_ALTERNATIVE) -> Fixed2dPoint(ROW_HEIGHT_AT_ALTERNATIVE_END, NOT_IMPORTANT),
        ViewMatrix.row(LAST_INDEX_INSIDE_ALTERNATIVE) -> Fixed2dPoint(ROW_HEIGHT_AT_ALTERNATIVE_END, NOT_IMPORTANT),
        ViewMatrix.width() -> Fixed2dPoint(SEQUENCE_DIAGRAM_WIDTH, NOT_IMPORTANT),
      )))

    points contains (
      Alternative.topLeft(ID) -> Fixed2dPoint(1, ROW_HEIGHT_BEFORE_ALTERNATIVE),
      Alternative.bottomRight(ID) -> Fixed2dPoint(SEQUENCE_DIAGRAM_WIDTH - 2, ROW_HEIGHT_AT_ALTERNATIVE_END),
      ViewMatrix.row(ALTERNATIVE_START_INDEX) -> Fixed2dPoint(ROW_HEIGHT_BEFORE_ALTERNATIVE + ALTERNATIVE_MIN_HEIGHT,
                                                              NOT_IMPORTANT),
    )
  }
}
