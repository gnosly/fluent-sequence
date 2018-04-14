package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.{Coordinates, Fixed2DPoint, FixedWidthFormatter, FixedWidthPainter}
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class FixedWidthFormatterTest extends FlatSpec with Matchers {
	val USER = new User("USER")
	val SYSTEM = new FluentActor("SYSTEM")

	val formatter = new FixedWidthFormatter(new FixedWidthPainter())


	it should "format actor with a auto-signal" in {

		val flow = Sequence("example").startWith(
			USER.does("something") :: Nil
		)


		val viewModel = generate(flow.toEventBook)

		formatter.format(viewModel) shouldBe mutable.TreeMap(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
			Coordinates.Activity.topLeft(0,0) -> Fixed2DPoint(3, 5),
			Coordinates.Activity.topRight(0,0) -> Fixed2DPoint(5, 5),
			Coordinates.Activity.rightPointStart(0,0,1) -> Fixed2DPoint(6, 6),
			Coordinates.Activity.rightPointEnd(0,0,1) -> Fixed2DPoint(6, 10),
			Coordinates.Activity.bottomLeft(0,0) -> Fixed2DPoint(3, 10)
		)
	}

	it should "format two actors" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		formatter.format(viewModel) shouldBe mutable.TreeMap(
			Coordinates.Actor.topLeft(0) -> Fixed2DPoint(1, 1),
			Coordinates.Actor.topRight(0) -> Fixed2DPoint(9, 1),
			Coordinates.Actor.bottomMiddle(0) -> Fixed2DPoint(4, 5),
			Coordinates.Actor.topLeft(1) -> Fixed2DPoint(19, 1),
			Coordinates.Actor.topRight(1) -> Fixed2DPoint(29, 1),
			Coordinates.Actor.bottomMiddle(1) -> Fixed2DPoint(23, 5),

			Coordinates.Activity.topLeft(0,0) -> Fixed2DPoint(3, 5),
			Coordinates.Activity.topRight(0,0) -> Fixed2DPoint(5, 5),
			Coordinates.Activity.rightPointStart(0,0,1) -> Fixed2DPoint(6, 6),
			Coordinates.Activity.rightPointEnd(0,0,1) -> Fixed2DPoint(6, 8),
			Coordinates.Activity.bottomLeft(0,0) -> Fixed2DPoint(3, 8),
			Coordinates.Activity.topLeft(1,0) -> Fixed2DPoint(22, 5),
			Coordinates.Activity.topRight(1,0) -> Fixed2DPoint(24, 5),
			Coordinates.Activity.leftPointStart(1,0,1) -> Fixed2DPoint(22, 6),
			Coordinates.Activity.leftPointEnd(1,0,1) -> Fixed2DPoint(22, 8),
			Coordinates.Activity.bottomLeft(1,0) -> Fixed2DPoint(22, 8)
		)
	}

	//test with more signal in sequence
	//test with signal in different actor sequence
	//test with more activity
}
