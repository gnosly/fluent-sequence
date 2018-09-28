package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModelComponents
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.HashMap
import scala.collection.mutable

class ViewModelFormatterTest extends FunSuite with Matchers {

  test("format") {
    val call = new SyncRequest("request call", 0, 0, 0, 1, 0)
    val response = new SyncResponse("response", 1, 1, 0, 0, 0)

    val userRightPoints = mutable.TreeMap[Int, RightPoint](
      0 -> ActivityPointForBiSignalOnTheRight(0, call),
      1 -> ActivityPointForBiSignalOnTheRight(1, response)
    )

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 1, rightPoints = userRightPoints)))

    val systemRightPoints = mutable.TreeMap[Int, LeftPoint](
      0 -> ActivityPointForBiSignalOnTheLeft(0, call),
      1 -> ActivityPointForBiSignalOnTheLeft(1, response)
    )

    val systemComponent =
      new ActorComponent(1, "system", asBuffer(new ActivityComponent(0, 0, 0, 1, leftPoints = systemRightPoints)))

    val viewModel = ViewModelComponents(mutable.HashMap("user" -> userComponent, "system" -> systemComponent))

    val formatter = new ViewModelFormatter(new FixedPreRenderer())

    val pointMap: Map[String, Fixed2dPoint] = formatter.format(viewModel)

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
      "actor_1_top_right" -> Fixed2dPoint(34, 3),
      "actor_1_activity_0_left_point_0_start" -> Fixed2dPoint(0, 1),
      "actor_1_activity_0_left_point_0_end" -> Fixed2dPoint(0, 3),
      "actor_1_top_left" -> Fixed2dPoint(24, 3),
      "actor_1_activity_0_left_point_1_start" -> Fixed2dPoint(0, 11),
      "actor_1_activity_0_left_point_1_end" -> Fixed2dPoint(0, 13),
      "actor_1_bottom_middle" -> Fixed2dPoint(28, 7),
      ViewMatrix.row(0) -> Fixed2dPoint(10, 0),
      ViewMatrix.row(1) -> Fixed2dPoint(13, 0),
      ViewMatrix.column(0) -> Fixed2dPoint(22, 0),
      ViewMatrix.column(1) -> Fixed2dPoint(5, 0)
    )

  }

  private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
    mutable.Buffer[ActivityComponent](component)
  }
}
