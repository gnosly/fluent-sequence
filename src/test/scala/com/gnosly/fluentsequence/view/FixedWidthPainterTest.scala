package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2DPoint, FixedWidthCanvas, FixedWidthPainter}
import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent}
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthPainterTest extends FlatSpec with Matchers {
	val fixedWidthPainter = new FixedWidthPainter()

	it should "render user box" in {
		val user = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val canvas = new FixedWidthCanvas()

		fixedWidthPainter.paint(user, Map(user.topLeftCornerId() -> Fixed2DPoint(1, 0)), canvas)

		canvas.print shouldBe
			/**/ " .------." + "\n" +
			/**/ " | user |" + "\n" +
			/**/ " '------'" + "\n" +
			/**/ "    |"
	}
}
