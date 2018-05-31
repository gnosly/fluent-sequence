package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthViewer
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class FixedWidthViewerTest extends FunSuite with Matchers {
	val USER = new User("user")
	val SYSTEM = new FluentActor("system")
	val SYSTEM_B = new FluentActor("another system")

	val viewer = new FixedWidthViewer()

	test("view an actor that calls another"){

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str shouldBe sequenceFromFile("two-actors-one-call.txt")
	}

	test("do a two actor sequence"){

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str shouldBe sequenceFromFile("two-actors.txt")
	}

	test("do a complete sequence"){

		val flow = Sequence("example").startWith(
			USER.does("something") ::
				USER.does("something else") ::
				USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)

		str shouldBe sequenceFromFile("complete-fixed-sequence.txt")
	}

	test("multi activity"){

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) ::
				USER.call("finalize", SYSTEM) ::
				SYSTEM.reply("finalize done", USER) ::
				Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str shouldBe sequenceFromFile("multi-activity.txt")
	}

	private def sequenceFromFile(filename: String) = {
		Source.fromResource(filename).mkString
	}

}
