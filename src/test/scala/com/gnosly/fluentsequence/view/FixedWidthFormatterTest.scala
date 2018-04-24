package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth._
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class FixedWidthFormatterTest extends FlatSpec with Matchers {
	val USER = new User("USER")
	val SYSTEM = new FluentActor("SYSTEM")

	val formatter = new FixedWidthFormatter(new FixedWidthPainter())


	def printThe(pointMap: mutable.TreeMap[String, Fixed2DPoint]) = {
		val canvas = new FixedWidthCanvas()

		var legendY = 2
		var charForPoint:String = "a"

		pointMap.foreach(
			p => {
				canvas.write(Fixed2DPoint(15,legendY),charForPoint + ": " + p._1)
				canvas.write(p._2, charForPoint)
				legendY+=1
				charForPoint = (charForPoint(0) + 1).toChar.toString
			}

		)

		println(canvas.print())
	}

	it should "format actor with a auto-signal" in {

		val flow = Sequence("example").startWith(
			USER.does("something") :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		val pointMap = formatter.format(viewModel)
		printThe(pointMap)
		pointMap shouldBe mutable.TreeMap(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 5),
			Coordinates.Activity.topRight(0, 0) -> Fixed2DPoint(5, 5),
			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 6),
			Coordinates.Activity.rightPointEnd(0, 0, 1) -> Fixed2DPoint(6, 10),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 10),
			Coordinates.endOfIndex(1) -> Fixed2DPoint(6, 10)
		)
	}

	it should "format two actors" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.call("reply", USER) :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		formatter.format(viewModel) shouldBe mutable.TreeMap(
			//Actor user
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
			//Actor system
			Coordinates.Actor.topLeft(1) -> Fixed2DPoint(19, 1),
			Coordinates.Actor.topRight(1) -> Fixed2DPoint(29, 1),
			Coordinates.Actor.bottomMiddle(1) -> Fixed2DPoint(23, 5),
			//Activity of actor user
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 5),
			Coordinates.Activity.topRight(0, 0) -> Fixed2DPoint(5, 5),
			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 6),
			Coordinates.Activity.rightPointEnd(0, 0, 1) -> Fixed2DPoint(6, 8),
			Coordinates.Activity.rightPointStart(0, 0, 2) -> Fixed2DPoint(6, 9),
			Coordinates.Activity.rightPointEnd(0, 0, 2) -> Fixed2DPoint(6, 11),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 11),
			//Activity of actor system
			Coordinates.Activity.topLeft(1, 0) -> Fixed2DPoint(22, 5),
			Coordinates.Activity.topRight(1, 0) -> Fixed2DPoint(24, 5),
			Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2DPoint(22, 6),
			Coordinates.Activity.leftPointEnd(1, 0, 1) -> Fixed2DPoint(22, 8),
			Coordinates.Activity.leftPointStart(1, 0, 2) -> Fixed2DPoint(22, 9),
			Coordinates.Activity.leftPointEnd(1, 0, 2) -> Fixed2DPoint(22, 11),
			Coordinates.Activity.bottomLeft(1, 0) -> Fixed2DPoint(22, 11),
			Coordinates.endOfIndex(1) -> Fixed2DPoint(6, 8),
			Coordinates.endOfIndex(2) -> Fixed2DPoint(6, 11)
		)
	}

	it should "format actor with a auto-signal and a call to another actor" in {

		val flow = Sequence("example").startWith(
			USER.does("something very very long") ::
				USER.call("call", SYSTEM) :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		println(viewModel)

		formatter.format(viewModel) shouldBe mutable.TreeMap(
			//Actor user
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
			//Actor system
			Coordinates.Actor.topLeft(1) -> Fixed2DPoint(39, 1),
			Coordinates.Actor.topRight(1) -> Fixed2DPoint(49, 1),
			Coordinates.Actor.bottomMiddle(1) -> Fixed2DPoint(43, 5),
			//Activity user
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 5),
			Coordinates.Activity.topRight(0, 0) -> Fixed2DPoint(5, 5),
			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 6),
			Coordinates.Activity.rightPointEnd(0, 0, 1) -> Fixed2DPoint(6, 10),
			Coordinates.Activity.rightPointStart(0, 0, 2) -> Fixed2DPoint(6, 11),
			Coordinates.Activity.rightPointEnd(0, 0, 2) -> Fixed2DPoint(6, 13),
			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 13),

			//Activity system
			Coordinates.Activity.topLeft(1, 0) -> Fixed2DPoint(42, 10),
			Coordinates.Activity.topRight(1, 0) -> Fixed2DPoint(44, 10),
			Coordinates.Activity.leftPointStart(1, 0, 2) -> Fixed2DPoint(42, 11),
			Coordinates.Activity.leftPointEnd(1, 0, 2) -> Fixed2DPoint(42, 13),
			Coordinates.Activity.bottomLeft(1, 0) -> Fixed2DPoint(42, 13),

			Coordinates.endOfIndex(1) -> Fixed2DPoint(6, 10),
			Coordinates.endOfIndex(2) -> Fixed2DPoint(6, 13)

		)
	}

	ignore should "format actor with multi activities" in {

		val flow = Sequence("example").startWith(
			USER.call("c", SYSTEM) ::
				SYSTEM.reply("r", USER) ::
				USER.call("c2", SYSTEM) ::
				SYSTEM.reply("r2", USER) ::
				Nil)

		val viewModel = generate(flow.toEventBook)

		println(viewModel)

		formatter.format(viewModel) shouldBe mutable.TreeMap(
			//Actor user
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
			//Actor system
			Coordinates.Actor.topLeft(1) -> Fixed2DPoint(19, 1),
			Coordinates.Actor.topRight(1) -> Fixed2DPoint(29, 1),
			Coordinates.Actor.bottomMiddle(1) -> Fixed2DPoint(23, 5),
			//Activity 0 user
			Coordinates.Activity.topLeft(0, 0) -> Fixed2DPoint(3, 5),
			Coordinates.Activity.topRight(0, 0) -> Fixed2DPoint(5, 5),
			//call
			Coordinates.Activity.rightPointStart(0, 0, 1) -> Fixed2DPoint(6, 6),
			Coordinates.Activity.rightPointEnd(0, 0, 1) -> Fixed2DPoint(6, 8),
			//reply
			Coordinates.Activity.rightPointStart(0, 0, 2) -> Fixed2DPoint(6, 9),
			Coordinates.Activity.rightPointEnd(0, 0, 2) -> Fixed2DPoint(6, 11),
			//second call
			Coordinates.Activity.rightPointStart(0, 0, 3) -> Fixed2DPoint(6, 12),
			Coordinates.Activity.rightPointEnd(0, 0, 3) -> Fixed2DPoint(6, 14),
			//second reply
			Coordinates.Activity.rightPointStart(0, 0, 4) -> Fixed2DPoint(6, 15),
			Coordinates.Activity.rightPointEnd(0, 0, 4) -> Fixed2DPoint(6, 17),

			Coordinates.Activity.bottomLeft(0, 0) -> Fixed2DPoint(3, 17),

			//Activity 0 system
			Coordinates.Activity.topLeft(1, 0) -> Fixed2DPoint(22, 5),
			Coordinates.Activity.topRight(1, 0) -> Fixed2DPoint(24, 5),
			//call
			Coordinates.Activity.leftPointStart(1, 0, 1) -> Fixed2DPoint(22, 6),
			Coordinates.Activity.leftPointEnd(1, 0, 1) -> Fixed2DPoint(22, 8),
			//reply
			Coordinates.Activity.leftPointStart(1, 0, 2) -> Fixed2DPoint(22, 9),
			Coordinates.Activity.leftPointEnd(1, 0, 2) -> Fixed2DPoint(22, 11),
			Coordinates.Activity.bottomLeft(1, 0) -> Fixed2DPoint(22, 11),

			//Activity 1 system
			Coordinates.Activity.topLeft(1, 1) -> Fixed2DPoint(22, 12),
			Coordinates.Activity.topRight(1, 1) -> Fixed2DPoint(24, 12),
			//second call
			Coordinates.Activity.leftPointStart(1, 1, 3) -> Fixed2DPoint(22, 12),
			Coordinates.Activity.leftPointEnd(1, 1, 3) -> Fixed2DPoint(22, 14),
			//second reply
			Coordinates.Activity.leftPointStart(1, 1, 4) -> Fixed2DPoint(22, 15),
			Coordinates.Activity.leftPointEnd(1, 1, 4) -> Fixed2DPoint(22, 17),
			Coordinates.Activity.bottomLeft(1, 1) -> Fixed2DPoint(22, 17),

			Coordinates.endOfIndex(1) -> Fixed2DPoint(6, 10),
			Coordinates.endOfIndex(2) -> Fixed2DPoint(6, 13)

		)
	}
	//test with signal in different actor sequence
	//test with more activity
}
