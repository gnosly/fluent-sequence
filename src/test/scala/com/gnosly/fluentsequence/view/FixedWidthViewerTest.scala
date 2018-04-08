package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthViewer
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class FixedWidthViewerTest extends FlatSpec with Matchers {
	val USER = new User("user")
	val SYSTEM = new FluentActor("system")

	val viewer = new FixedWidthViewer()

	ignore should "do a two actor sequence" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str shouldBe load("two-actors.txt")
	}

	ignore should "do a complete sequence" in {

		val flow = Sequence("example").startWith(
			USER.does("something") ::
				USER.does("something else") ::
				USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		viewer.view(flow.toEventBook) shouldBe load("complete-fixed-sequence.txt")
	}

	private def load(filename: String) = {
		Source.fromResource(filename).mkString
	}

}
