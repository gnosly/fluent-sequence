package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2DPoint, FixedWidthFormatter, FixedWidthPainter}
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthFormatterTest extends FlatSpec with Matchers {
	val USER = new User("USER")
	val SYSTEM = new FluentActor("SYSTEM")

	val formatter = new FixedWidthFormatter(new FixedWidthPainter())

	it should "format two actors" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		formatter.format(viewModel) shouldBe Map(
			"actor_0_top_left" -> Fixed2DPoint(1, 1),
			"actor_0_top_right" -> Fixed2DPoint(9, 1),
			"actor_1_top_left" -> Fixed2DPoint(19, 1),
			"actor_1_top_right" -> Fixed2DPoint(29, 1)
		)
	}

	it should "format actor with a autosignal" in {

		val flow = Sequence("example").startWith(
			USER.does("something") :: Nil
		)

		val viewModel = generate(flow.toEventBook)

		formatter.format(viewModel) shouldBe Map(
			"actor_0_top_left" -> Fixed2DPoint(1, 1),
			"actor_0_top_right" -> Fixed2DPoint(9, 1),
			"actor_0_activity_0_top_left" -> Fixed2DPoint(19, 1),
			"actor_0_activity_0_right_point_0" -> Fixed2DPoint(29, 1),
			"actor_0_activity_0_right_point_1" -> Fixed2DPoint(29, 1)
		)
	}
}
