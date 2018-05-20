package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.fixedwidth.{Box, _}
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class FixedWidthFormatterTest extends FlatSpec with Matchers {
	val USER = new User("USER")
	val SYSTEM = new FluentActor("SYSTEM")

	val formatter = new FixedWidthFormatter(new FixedWidthPainter())

	it should "format first actor alone" in {
		formatter.formatActor(new ActorComponent(0, "user", null)) shouldBe ActorPoints(0, new Fixed2DPoint(1, 1), Box(8, 4))
	}

	it should "format second actor alone" in {
		formatter.formatActor(new ActorComponent(1, "user", null)) shouldBe
			ActorPoints(1,
				new ReferencePoint(Actor.topRight(0))
					.right(PointMath.max(Reference1DPoint(s"column_${0}"), Fixed1DPoint(DISTANCE_BETWEEN_ACTORS))),
				Box(8, 4)
			)
	}

	it should "format first activity" in {
		val lastSignalIndex = 3
		formatter.formatActivity(new ActivityComponent(0, 0, 0, lastSignalIndex, true, null, null)) shouldBe
			ActivityPoints(0, 0, new ReferencePoint(Actor.bottomMiddle(0)).left(1), 2, Reference1DPoint(ViewMatrix.row(lastSignalIndex)))
	}

	it should "format actor with a auto-signal" in {

		val flow = Sequence("example").startWith(
			USER.does("something") :: Nil
		)

		val pointMap = formatter.format(generate(flow.toEventBook))
		printThe(pointMap)

		val actorPoints = new ActorPoints(0, new Fixed2DPoint(1, 1), Box(8, 4))
		val activityPoints = ActivityPoints(0, 0, new Fixed2DPoint(3, 5), 2, Fixed1DPoint(5))
		val signalPoints = new SignalPoint(0, 0, 1, Box(5, 4), "right", activityPoints.activityTopRight.down(1).right(1))

		pointMap should contain theSameElementsAs (
			actorPoints.toPoints(null) ++ activityPoints.toPoints(null) ++ signalPoints.toPoints(null)
			)
	}
	//
	//	it should "format two actors" in {
	//
	//		val flow = Sequence("example").startWith(
	//			USER.call("call", SYSTEM) ::
	//				SYSTEM.call("reply", USER) :: Nil
	//		)
	//
	//		val viewModel = generate(flow.toEventBook)
	//
	//		val pointMap = formatter.format(viewModel)
	//		printThe(pointMap)
	//		pointMap shouldBe mutable.TreeMap(
	//			//Actor user
	//			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
	//			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
	//			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
	//			//Actor system
	//			Coordinates.Actor.topLeft(1) -> Fixed2DPoint(19, 1),
	//			Coordinates.Actor.topRight(1) -> Fixed2DPoint(29, 1),
	//			Coordinates.Actor.bottomMiddle(1) -> Fixed2DPoint(23, 5),
	//			//Activity of actor user
	//			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 5),
	//			Coordinates.Activity.topRight(0, 0) -> Fixed2DPoint(5, 5),
	//			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 6),
	//			Coordinates.Activity.rightPointEnd(0, 0, 1) -> Fixed2DPoint(6, 8),
	//			Coordinates.Activity.rightPointStart(0, 0, 2) -> Fixed2DPoint(6, 9),
	//			Coordinates.Activity.rightPointEnd(0, 0, 2) -> Fixed2DPoint(6, 11),
	//			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 11),
	//			//Activity of actor system
	//			Coordinates.Activity.topLeft(1, 0) -> Fixed2DPoint(22, 5),
	//			Coordinates.Activity.topRight(1, 0) -> Fixed2DPoint(24, 5),
	//			Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2DPoint(22, 6),
	//			Coordinates.Activity.leftPointEnd(1, 0, 1) -> Fixed2DPoint(22, 8),
	//			Coordinates.Activity.leftPointStart(1, 0, 2) -> Fixed2DPoint(22, 9),
	//			Coordinates.Activity.leftPointEnd(1, 0, 2) -> Fixed2DPoint(22, 11),
	//			Coordinates.Activity.bottomLeft(1, 0) -> Fixed2DPoint(22, 11)
	//		)
	//	}
	//
	//	it should "format actor with a auto-signal and a call to another actor" in {
	//
	//		val flow = Sequence("example").startWith(
	//			USER.does("something very very long") ::
	//				USER.call("call", SYSTEM) :: Nil
	//		)
	//
	//		val viewModel = generate(flow.toEventBook)
	//
	//		println(viewModel)
	//
	//		val pointMap = formatter.format(viewModel)
	//		printThe(pointMap)
	//		pointMap shouldBe mutable.TreeMap(
	//			//Actor user
	//			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
	//			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
	//			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
	//			//Actor system
	//			Coordinates.Actor.topLeft(1) -> Fixed2DPoint(39, 1),
	//			Coordinates.Actor.topRight(1) -> Fixed2DPoint(49, 1),
	//			Coordinates.Actor.bottomMiddle(1) -> Fixed2DPoint(43, 5),
	//			//Activity user
	//			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 5),
	//			Coordinates.Activity.topRight(0, 0) -> Fixed2DPoint(5, 5),
	//			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 6),
	//			Coordinates.Activity.rightPointEnd(0, 0, 1) -> Fixed2DPoint(6, 10),
	//			Coordinates.Activity.rightPointStart(0, 0, 2) -> Fixed2DPoint(6, 11),
	//			Coordinates.Activity.rightPointEnd(0, 0, 2) -> Fixed2DPoint(6, 13),
	//			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 13),
	//
	//			//Activity system
	//			Coordinates.Activity.topLeft(1, 0) -> Fixed2DPoint(42, 10),
	//			Coordinates.Activity.topRight(1, 0) -> Fixed2DPoint(44, 10),
	//			Coordinates.Activity.leftPointStart(1, 0, 2) -> Fixed2DPoint(42, 11),
	//			Coordinates.Activity.leftPointEnd(1, 0, 2) -> Fixed2DPoint(42, 13),
	//			Coordinates.Activity.bottomLeft(1, 0) -> Fixed2DPoint(42, 13)
	//
	//		)
	//	}
	//
	//	it should "format actor with multi activities" in {
	//
	//		val flow = Sequence("example").startWith(
	//			USER.call("c", SYSTEM) ::
	//				SYSTEM.reply("r", USER) ::
	//				USER.call("c2", SYSTEM) ::
	//				SYSTEM.reply("r2", USER) ::
	//				Nil)
	//
	//		val viewModel = generate(flow.toEventBook)
	//
	//		println(viewModel)
	//
	//		val pointMap = formatter.format(viewModel)
	//		printThe(pointMap)
	//
	//		val expectedPointMap: Seq[(String, Fixed2DPoint)] =
	//		//actor #0
	//			new ActorPoints(0, Fixed2DPoint(1, 1), Box(8, 4)).toPoints(null, null) ++
	//				new ActivityPoints(0, 0, Fixed2DPoint(3, 5), Box(2, 13)).toPoints() ++
	//				new SignalPoint(0, 0, 1, Box(5, 2), "right", Fixed2DPoint(6, 6)).toPoints() ++
	//				new SignalPoint(0, 0, 2, Box(5, 2), "right", Fixed2DPoint(6, 9)).toPoints() ++
	//				new SignalPoint(0, 0, 3, Box(5, 2), "right", Fixed2DPoint(6, 13)).toPoints() ++
	//				new SignalPoint(0, 0, 4, Box(5, 2), "right", Fixed2DPoint(6, 16)).toPoints() ++
	//				//actor #1
	//				new ActorPoints(1, Fixed2DPoint(19, 1), Box(10, 4)).toPoints(null, null) ++
	//				new ActivityPoints(1, 0, Fixed2DPoint(22, 5), Box(2, 6)).toPoints() ++
	//				new SignalPoint(1, 0, 1, Box(5, 2), "left", Fixed2DPoint(22, 6)).toPoints() ++
	//				new SignalPoint(1, 0, 2, Box(5, 2), "left", Fixed2DPoint(22, 9)).toPoints() ++
	//				new ActivityPoints(1, 1, Fixed2DPoint(22, 12), Box(2, 6)).toPoints() ++
	//				new SignalPoint(1, 1, 3, Box(5, 2), "left", Fixed2DPoint(22, 13)).toPoints() ++
	//				new SignalPoint(1, 1, 4, Box(5, 2), "left", Fixed2DPoint(22, 16)).toPoints()
	//
	//		pointMap.toMap should contain theSameElementsAs expectedPointMap.toMap
	//	}


	private def printThe(pointMap: mutable.TreeMap[String, VeryFixed2dPoint]): String = {
		val canvas = new FixedWidthCanvas()

		var charForPoint: String = "a"

		pointMap.foreach(
			p => {
				println(charForPoint + ": " + p._1)
				canvas.write(p._2, charForPoint)
				charForPoint = (charForPoint(0) + 1).toChar.toString
			}

		)

		println("--------------")
		val str = canvas.print()
		println(str)
		println("--------------")

		str
	}

	//test with signal in different actor sequence
}
