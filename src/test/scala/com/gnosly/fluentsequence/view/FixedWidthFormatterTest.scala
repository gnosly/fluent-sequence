package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.Fixed2DPoint
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthFormatterTest extends FlatSpec with Matchers {
	val USER = new User("USER")
	val SYSTEM = new FluentActor("SYSTEM")


	it should "format single actor" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val formatter = new FixedWidthFormatter(generate(flow.toEventBook), new FixedWidthPainter())
		formatter.format() shouldBe Map(
			"actor_0_top_left" -> Fixed2DPoint(0, 0),
			"actor_0_top_right" -> Fixed2DPoint(8, 0),
			"actor_1_top_left" -> Fixed2DPoint(13, 0),
			"actor_1_top_right" -> Fixed2DPoint(23, 0)
		)
	}
}
