package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.fixedwidth.{Coordinates, Fixed2DPoint, FixedWidthCanvas, FixedWidthPainter}
import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class FixedWidthPainterTest extends FlatSpec with Matchers {
	val painter = new FixedWidthPainter()


	it should "render user box" in {
		val user = new ActorComponent(0, "user")
		user.done("something", 0)
		val canvas = new FixedWidthCanvas()

		painter.paint(user, Map(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 0),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 8),
			Coordinates.Activity.rightPointStart(0, 0, 0) -> Fixed2DPoint(6, 5)
		), canvas)

		val print = canvas.print
		println(print)
		print shouldBe
			/**/ " .------." + "\n" +
			/**/ " | user |" + "\n" +
			/**/ " '------'" + "\n" +
			/**/ "    |" + "\n" +
			/**/ "   _|_" + "\n" +
			/**/ "   | |____" + "\n" +
			/**/ "   | |    |" + "\n" +
			/**/ "   | |    | something" + "\n" +
			/**/ "   |_|<---'" + "\n" +
			/**/ "    |"

	}

	it should "render user box with a bisignal exiting" in {
		val user = new ActorComponent(0, "user")
		val system = new ActorComponent(1, "user")
		user.link(system, "something", 1)

		val canvas = new FixedWidthCanvas()

		painter.paint(user, Map(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 0),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 7),
			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 5),
			Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2DPoint(20, 5)
		), canvas)

		val print = canvas.print
		println(print)
		print shouldBe
			/**/ " .------." + "\n" +
			/**/ " | user |" + "\n" +
			/**/ " '------'" + "\n" +
			/**/ "    |" + "\n" +
			/**/ "   _|_" + "\n" +
			/**/ "   | |  something" + "\n" +
			/**/ "   | |------------->" + "\n" +
			/**/ "   |_|" + "\n" +
			/**/ "    |"

	}

	it should "render user box with a bisignal entering" in {
		val user = new ActorComponent(0, "user")
		val system = new ActorComponent(1, "user")
		system.link(user, "something", 1)

		val canvas = new FixedWidthCanvas()

		painter.paint(user, Map(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 0),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 7),
			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 5),
			Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2DPoint(20, 5)
		), canvas)

		val print = canvas.print
		println(print)
		print shouldBe
			/**/ " .------." + "\n" +
			/**/ " | user |" + "\n" +
			/**/ " '------'" + "\n" +
			/**/ "    |" + "\n" +
			/**/ "   _|_" + "\n" +
			/**/ "   | |  something" + "\n" +
			/**/ "   | |<-------------" + "\n" +
			/**/ "   |_|" + "\n" +
			/**/ "    |"

	}

	it should "render user box with many activities" in {
		val user = new ActorComponent(0, "user", mutable.Buffer(
			new ActivityComponent(0,0, 0,3,true),
			new ActivityComponent(1,0, 5,8,true)
		))

		val canvas = new FixedWidthCanvas()

		painter.paint(user, Map(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 0),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 4),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 7),
			Coordinates.Activity.topLeft(0, 1) -> Fixed2DPoint(3, 9),
			Coordinates.Activity.bottomLeft(0, 1) -> Fixed2DPoint(3, 12)
		), canvas)

		val print = canvas.print
		println(print)
		print shouldBe
			/**/ " .------." + "\n" +
			/**/ " | user |" + "\n" +
			/**/ " '------'" + "\n" +
			/**/ "    |" + "\n" +
			/**/ "   _|_" + "\n" +
			/**/ "   | |" + "\n" +
			/**/ "   | |" + "\n" +
			/**/ "   |_|" + "\n" +
			/**/ "    |" + "\n" +
			/**/ "   _|_" + "\n" +
			/**/ "   | |" + "\n" +
			/**/ "   | |" + "\n" +
			/**/ "   |_|" + "\n" +
			/**/ "    |"

	}
}
