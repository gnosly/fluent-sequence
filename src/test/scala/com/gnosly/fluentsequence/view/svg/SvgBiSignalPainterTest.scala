package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.component.BiSignalComponent
import com.gnosly.fluentsequence.view.{Coordinates, Fixed2dPoint}
import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.HashMap

class SvgBiSignalPainterTest extends FunSuite with Matchers{
	val painter = new SvgBiSignalPainter()

	test("bisignal left to write"){
		val pointMap: Map[String, Fixed2dPoint] = HashMap(
			Coordinates.Activity.rightPointStart(0, 0,0) -> new Fixed2dPoint(0, 10),
			Coordinates.Activity.leftPointStart(1, 0,0) -> new Fixed2dPoint(10, 10)
		)

		val canvas = painter.paint(new BiSignalComponent("name",0, 0, 0, 1,0), pointMap)
		println(canvas)
		canvas.content() shouldBe
			"""<text x="30" y="100" font-size="16px">name</text>
				|<line x1="0" y1="110" x2="100" y2="110" style="stroke:black;stroke-width:2;" />
				|<polyline fill="none" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" points="90,100 100,110 90,120"/>
				|""".stripMargin
	}
}
