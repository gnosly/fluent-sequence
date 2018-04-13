package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.fixedwidth.{Coordinates, Fixed2DPoint, FixedWidthCanvas, FixedWidthPainter}
import com.gnosly.fluentsequence.view.model.ActorComponent
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthPainterTest extends FlatSpec with Matchers {
	val painter = new FixedWidthPainter()

	it should "render user box" in {
		val user = new ActorComponent(0, "user", 0)
		user.done("something", 0)
		val canvas = new FixedWidthCanvas()

		painter.paint(user, Map(
			Coordinates.topLeftCornerIdForActor(0) -> Fixed2DPoint(1, 0),
			Coordinates.Activity.topLeftCorner(0,0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.bottomLeftCorner(0,0) -> Fixed2DPoint(3, 8),
			Coordinates.Activity.point(0,0,0,"right") -> Fixed2DPoint(6, 5)
		), canvas)

		val print = canvas.print
		println(print)
		print shouldBe
			/**/ " .------." + "\n" +
			/**/ " | user |" + "\n" +
			/**/ " '------'" + "\n" +
			/**/ "    |"  + "\n" +
			/**/ "   _|_" + "\n" +
			/**/ "   | |____" + "\n" +
			/**/ "   | |    |" + "\n" +
			/**/ "   | |    | something" + "\n" +
			/**/ "   |_|<---'" + "\n" +
			/**/ "    |"

	}
}
