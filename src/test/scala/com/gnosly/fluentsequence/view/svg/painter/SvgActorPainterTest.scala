package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.immutable.HashMap

class SvgActorPainterTest extends FunSuite with Matchers {
  val painter = new SvgActorPainter

  test("actor") {

    val pointMap = new ResolvedPoints(
      Map(
        Coordinates.Actor.topLeft(0) -> Fixed2dPoint(1, 1),
        Coordinates.Actor.bottomMiddle(0) -> Fixed2dPoint(4, 5)
      ))

    val canvas = painter.paint(ActorModel(0, "name", true), pointMap)
    println(canvas)

    canvas.content shouldBe
      """<rect x="10" y="10" width="60" height="30" style="stroke-width: 2.0;stroke: black;fill: white" />
				|<text x="40" y="30" font-size="16px" text-anchor="middle">name</text>
				|""".stripMargin
  }
}
