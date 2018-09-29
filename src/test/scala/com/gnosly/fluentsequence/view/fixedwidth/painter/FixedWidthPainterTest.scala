package com.gnosly.fluentsequence.view.fixedwidth.painter

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class FixedWidthPainterTest extends FlatSpec with Matchers {
  val painter = new FixedWidthPainter

  ignore should "render user box with a bisignal exiting" in {
//		val user = new ActorComponent(0, "user")
//		val system = new ActorComponent(1, "user")
//		user.link(system, "something", 1)
//
//		val canvas = new FixedWidthCanvas
//
//		painter.paint(user, canvas, Map(
//					Coordinates.Actor.topLeft(0) -> Fixed2dPoint(1, 0),
//					Coordinates.Actor.bottomMiddle(0) -> Fixed2dPoint(4, 4),
//					Coordinates.Activity.topLeft(0, 0) -> Fixed2dPoint(3, 4),
//					Coordinates.Activity.bottomLeft(0, 0) -> Fixed2dPoint(3, 7),
//					Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2dPoint(6, 5),
//					Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2dPoint(20, 5)
//				))
//
//		val print = canvas.print()
//		println(print)
//		print shouldBe
//			/**/ " .------." + "\n" +
//			/**/ " | user |" + "\n" +
//			/**/ " '------'" + "\n" +
//			/**/ "    |" + "\n" +
//			/**/ "   _|_" + "\n" +
//			/**/ "   | |  something" + "\n" +
//			/**/ "   | |------------->" + "\n" +
//			/**/ "   |_|" + "\n" +
//			/**/ "    |"

  }

  ignore should "render user box with a bisignal entering" in {
//		val user = new ActorComponent(0, "user")
//		val system = new ActorComponent(1, "user")
//		system.link(user, "something", 1)
//
//		val canvas = new FixedWidthCanvas
//
//		painter.paint(user, canvas, Map(
//					Coordinates.Actor.topLeft(0) -> Fixed2dPoint(1, 0),
//					Coordinates.Actor.bottomMiddle(0) -> Fixed2dPoint(4, 4),
//					Coordinates.Activity.topLeft(0, 0) -> Fixed2dPoint(3, 4),
//					Coordinates.Activity.bottomLeft(0, 0) -> Fixed2dPoint(3, 7),
//					Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2dPoint(6, 5),
//					Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2dPoint(20, 5)
//				))
//
//		val print = canvas.print()
//		println(print)
//		print shouldBe
//			/**/ " .------." + "\n" +
//			/**/ " | user |" + "\n" +
//			/**/ " '------'" + "\n" +
//			/**/ "    |" + "\n" +
//			/**/ "   _|_" + "\n" +
//			/**/ "   | |  something" + "\n" +
//			/**/ "   | |<-------------" + "\n" +
//			/**/ "   |_|" + "\n" +
//			/**/ "    |"

  }

  ignore should "render user box with many activities" in {
//		val user = new ActorComponent(0, "user", mutable.Buffer(
//			new ActivityComponent(0,0, 0,3,true),
//			new ActivityComponent(1,0, 5,8,true)
//		))
//
//		val canvas = new FixedWidthCanvas
//
//		painter.paint(user, canvas, Map(
//					Coordinates.Actor.topLeft(0) -> Fixed2dPoint(1, 0),
//					Coordinates.Actor.bottomMiddle(0) -> Fixed2dPoint(4, 4),
//					Coordinates.Activity.topLeft(0, 0) -> Fixed2dPoint(3, 4),
//					Coordinates.Activity.bottomLeft(0, 0) -> Fixed2dPoint(3, 7),
//					Coordinates.Activity.topLeft(0, 1) -> Fixed2dPoint(3, 9),
//					Coordinates.Activity.bottomLeft(0, 1) -> Fixed2dPoint(3, 12)
//				))
//
//		val print = canvas.print()
//		println(print)
//		print shouldBe
//			/**/ " .------." + "\n" +
//			/**/ " | user |" + "\n" +
//			/**/ " '------'" + "\n" +
//			/**/ "    |" + "\n" +
//			/**/ "   _|_" + "\n" +
//			/**/ "   | |" + "\n" +
//			/**/ "   | |" + "\n" +
//			/**/ "   |_|" + "\n" +
//			/**/ "    |" + "\n" +
//			/**/ "   _|_" + "\n" +
//			/**/ "   | |" + "\n" +
//			/**/ "   | |" + "\n" +
//			/**/ "   |_|" + "\n" +
//			/**/ "    |"

  }
}
