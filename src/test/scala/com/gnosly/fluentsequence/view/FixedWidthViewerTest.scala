package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthViewer
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class FixedWidthViewerTest extends FlatSpec with Matchers {
	val USER = new User("user")
	val SYSTEM = new FluentActor("system")
	val SYSTEM_B = new FluentActor("another system")

	val viewer = new FixedWidthViewer()

	it should "do a actor that calls another" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str shouldBe sequenceFromFile("two-actors-one-call.txt")
	}

	it should "do a two actor sequence" in {

		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) ::
				SYSTEM.reply("reply", USER) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str shouldBe sequenceFromFile("two-actors.txt")
	}

	it should "do a complete sequence" in {

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

	it should "multi activity" in {

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
