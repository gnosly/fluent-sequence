package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.svg.painter.SvgAutoSignalPainter
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.HashMap

class SvgAutoSignalPainterTest extends FunSuite with Matchers {
	val painter = new SvgAutoSignalPainter()

	test("simple autosignal") {

		val pointMap: Map[String, Fixed2dPoint] = HashMap(
			Coordinates.Activity.rightPointStart(0, 0, 0) -> new Fixed2dPoint(1, 10)
		)

		val canvas = painter.paint(new AutoSignalComponent("name", 0, 0, 0), pointMap)
		println(canvas)
		canvas.content() shouldBe """<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="0,100 20,100 20,130 2,130"/>
																|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="12,125 2,130 12,135"/>
																|<text x="40" y="120" font-size="16px" text-anchor="start">name</text>
																|""".stripMargin

	}
}
