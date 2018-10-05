package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory.viewModelFrom
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap
import scala.collection.mutable

class ViewModelFormatterTest extends FunSuite with Matchers {
  val SYSTEM = Actor(SEQUENCE_ACTOR_TYPE(), "system")
  val USER = Actor(USER_TYPE(), "user")

  test("format") {
    val viewModel = viewModelFrom(
      EventBook(
        CALLED(USER, "call", SYSTEM),
        REPLIED(SYSTEM, "response", USER)
      ))
    val formatter = new ViewModelFormatter(new FixedPreRenderer())

    val pointMap = formatter.format(viewModel)

    pointMap shouldBe HashMap(
      "actor_0_activity_0_bottom_left" -> Fixed2dPoint(4, 13),
      "actor_0_activity_0_right_point_0_start" -> Fixed2dPoint(7, 8),
      "actor_0_activity_0_right_point_0_end" -> Fixed2dPoint(7, 10),
      "actor_0_activity_0_top_left" -> Fixed2dPoint(4, 7),
      "actor_0_activity_0_top_right" -> Fixed2dPoint(6, 7),
      "actor_0_top_left" -> Fixed2dPoint(2, 3),
      "actor_0_activity_0_right_point_1_end" -> Fixed2dPoint(7, 13),
      "actor_0_bottom_middle" -> Fixed2dPoint(5, 7),
      "actor_0_activity_0_right_point_1_start" -> Fixed2dPoint(7, 11),
      "actor_0_top_right" -> Fixed2dPoint(10, 3),
      "actor_1_top_left" -> Fixed2dPoint(20, 3),
      "actor_1_top_right" -> Fixed2dPoint(30, 3),
      "actor_1_activity_0_top_left" -> Fixed2dPoint(23, 7),
      "actor_1_activity_0_left_point_0_start" -> Fixed2dPoint(23, 8),
      "actor_1_activity_0_left_point_0_end" -> Fixed2dPoint(23, 10),
      "actor_1_activity_0_left_point_1_start" -> Fixed2dPoint(23, 11),
      "actor_1_activity_0_left_point_1_end" -> Fixed2dPoint(23, 13),
      "actor_1_activity_0_bottom_left" -> Fixed2dPoint(23, 13),
      "actor_1_activity_0_top_right" -> Fixed2dPoint(25, 7),
      "actor_1_bottom_middle" -> Fixed2dPoint(24, 7),
      ViewMatrix.row(0) -> Fixed2dPoint(10, 0),
      ViewMatrix.row(1) -> Fixed2dPoint(13, 0),
      ViewMatrix.column(0) -> Fixed2dPoint(18, 0),
      ViewMatrix.column(1) -> Fixed2dPoint(10, 0),
      ViewMatrix.width() -> Fixed2dPoint(31, 0),
      ViewMatrix.height() -> Fixed2dPoint(16, 0),
    )

  }
}
