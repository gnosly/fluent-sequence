package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_MIN_HEIGHT
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.TOP_MARGIN
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RowsFormatterTest extends FunSuite with Matchers {
  val ACTOR_ROW = -1
  val SIGNAL_INDEX = 1
  val ACTOR_ID = 3
  val ACTIVITY_ID = 56
  val formatter = new RowsFormatter()

  test("actor row") {
    val points = formatter.format(List()).flatMap(_.toPoints(ResolvedPoints(Map())))

    points contains (
      ViewMatrix.row(ACTOR_ROW) -> Fixed2dPoint(TOP_MARGIN + ACTOR_MIN_HEIGHT, 0)
    )
  }

  test("autosignal row") {
    val NOT_IMPORTANT = 30
    val POINT_END_HEIGHT = 35

    val points =
      formatter
        .format(AutoSignalModel("", SIGNAL_INDEX, ACTOR_ID, ACTIVITY_ID) :: Nil)
        .flatMap(
          _.toPoints(
            ResolvedPoints(
              Map(
                Activity.pointEnd(ACTOR_ID, ACTIVITY_ID, SIGNAL_INDEX, "right") -> Fixed2dPoint(NOT_IMPORTANT,
                                                                                                POINT_END_HEIGHT)
              ))))

    points contains (
      ViewMatrix.row(ACTOR_ROW) -> Fixed2dPoint(TOP_MARGIN + ACTOR_MIN_HEIGHT, 0),
      ViewMatrix.row(SIGNAL_INDEX) -> Fixed2dPoint(POINT_END_HEIGHT, 0)
    )
  }
//
//  test("signal right") {
//
//    val point = formatter.format(new SyncRequest("", SIGNAL_INDEX, 0, 0, 1, 1))
//
//    point shouldBe RowPoint(SIGNAL_INDEX, new ReferencePoint(Activity.pointEnd(0, 0, SIGNAL_INDEX, "right")).y)
//  }
//
//  test("signal left") {
//
//    val point = formatter.format(new SyncResponse("", SIGNAL_INDEX, 1, 1, 0, 0))
//
//    point shouldBe RowPoint(SIGNAL_INDEX, new ReferencePoint(Activity.pointEnd(1, 1, SIGNAL_INDEX, "left")).y)
//  }
}
