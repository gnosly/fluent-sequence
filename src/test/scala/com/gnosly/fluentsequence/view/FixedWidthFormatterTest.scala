package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2DPoint, FixedWidthFormatter, FixedWidthPainter}
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
			"actor_0_top_left" -> Fixed2DPoint(1, 1),
			"actor_0_top_right" -> Fixed2DPoint(9, 1),
			"actor_0_bottom_middle" -> Fixed2DPoint(4, 5),
			"actor_0_activity_0_top_left" -> Fixed2DPoint(3, 5),
			"actor_0_activity_0_top_right" -> Fixed2DPoint(5, 5),
			"actor_0_activity_0_right_point_0" -> Fixed2DPoint(5, 6)
		)
	}

	it should "format two actors" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		formatter.format(viewModel) shouldBe mutable.TreeMap(
			"actor_0_top_left" -> Fixed2DPoint(1, 1),
			"actor_0_top_right" -> Fixed2DPoint(9, 1),
			"actor_0_bottom_middle" -> Fixed2DPoint(4, 5),
			"actor_1_top_left" -> Fixed2DPoint(19, 1),
			"actor_1_top_right" -> Fixed2DPoint(29, 1),
			"actor_1_bottom_middle" -> Fixed2DPoint(23, 5),

			"actor_0_activity_0_top_left" -> Fixed2DPoint(3, 5),
			"actor_0_activity_0_top_right" -> Fixed2DPoint(5, 5),
			"actor_0_activity_0_right_point_0" -> Fixed2DPoint(5, 6),
			"actor_1_activity_0_top_left" -> Fixed2DPoint(22, 5),
			"actor_1_activity_0_top_right" -> Fixed2DPoint(24, 5),
			"actor_1_activity_0_left_point_0" -> Fixed2DPoint(19, 6)
		)
	}

}
