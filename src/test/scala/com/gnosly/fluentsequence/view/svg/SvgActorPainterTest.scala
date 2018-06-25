package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.{Coordinates, Fixed2dPoint}
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.HashMap

class SvgActorPainterTest extends FunSuite with Matchers {
	val painter = new SvgActorPainter()

	test("actor") {

		var pointMap: Map[String, Fixed2dPoint] = HashMap(
			Coordinates.Actor.topLeft(0) -> new Fixed2dPoint(1, 1),
			Coordinates.Actor.bottomMiddle(0) -> new Fixed2dPoint(4, 5)
		)

		painter.paint(new ActorComponent(0, "name"), pointMap).content() shouldBe
			"""<rect x="10" y="10" width="60" height="30" style="stroke-width: 2.0;stroke: black;fill: none" /><text x="20" y="30" font-size="16px">name</text>"""
	}
}
