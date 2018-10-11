package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ActivityModel
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap

class SvgActivityPainterTest extends FunSuite with Matchers {
  val painter = new SvgActivityPainter

  test("first activity") {
    val pointMap = HashMap(
      Coordinates.Actor.topLeft(0) -> Fixed2dPoint(1, 1),
      Coordinates.Actor.bottomMiddle(0) -> Fixed2dPoint(4, 5),
      Coordinates.Activity.topLeft(0, 0) -> Fixed2dPoint(3, 5),
      Coordinates.Activity.bottomLeft(0, 0) -> Fixed2dPoint(3, 8)
    )

    val canvas = painter.paint(ActivityModel(0, 0, 0, 10), pointMap)

    canvas.content shouldBe
      """<line x1="40" y1="40" x2="40" y2="90" style="stroke:black;stroke-width:2;stroke-dasharray:5,5" />
				|<rect x="30" y="50" width="20" height="30" style="stroke-width: 2.0;stroke: black;fill: white" />
				|""".stripMargin
  }

  test("second activity") {
    val pointMap = HashMap(
      Coordinates.Activity.bottomLeft(0, 0) -> Fixed2dPoint(3, 5),
      Coordinates.Activity.topLeft(0, 1) -> Fixed2dPoint(3, 15),
      Coordinates.Activity.bottomLeft(0, 1) -> Fixed2dPoint(3, 25)
    )

    val canvas = painter.paint(ActivityModel(1, 0, 5, 10), pointMap)

    canvas.content shouldBe
      """<line x1="40" y1="60" x2="40" y2="260" style="stroke:black;stroke-width:2;stroke-dasharray:5,5" />
				|<rect x="30" y="150" width="20" height="100" style="stroke-width: 2.0;stroke: black;fill: white" />
				|""".stripMargin
  }

  ignore("last activity") {}
}
