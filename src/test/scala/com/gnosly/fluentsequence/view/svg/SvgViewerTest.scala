package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class SvgViewerTest extends FunSuite with Matchers {
	val USER = new User("user")
	val SYSTEM = new FluentActor("system")
	val viewer = new SvgViewer()

	test("view an actor that calls another") {
		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str.print() shouldBe sequenceFromFile("svg/two-actors-one-call.svg")
	}

	test("do a two actor sequence") {
		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str.print() shouldBe sequenceFromFile("svg/two-actors.svg")
	}

	test("do a complete sequence") {

		val flow = Sequence("example").startWith(
			USER.does("something") ::
				USER.does("something else") ::
				USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)

		str.print() shouldBe sequenceFromFile("svg/complete-fixed-sequence.svg")
	}

	test("multi activity") {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) ::
				USER.call("finalize", SYSTEM) ::
				SYSTEM.reply("finalize done", USER) ::
				Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str.print() shouldBe sequenceFromFile("svg/multi-activity.svg")
	}


	private def sequenceFromFile(filename: String) = {
		Source.fromResource(s"$filename").mkString
	}
}
