package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class FixedWidthGoldenMasterTest extends FlatSpec with Matchers {

	"FixedViewer" should "do a two actor sequence" in {
		val user = new User("user")
		val system = new FluentActor("system")

		val flow = new Sequence("example").startWith(
			user.call("call", system) ::
				system.reply("reply", user) :: Nil
		)

		val expected = load("two-actors.txt")

		val viewer = new FixedWidthViewer()
		viewer.view(flow.toEventBook) shouldBe expected
	}

	ignore should "do a complete sequence" in {
		val user = new User("user")
		val system = new FluentActor("system")

		val flow = new Sequence("example").startWith(
			user.does("something") ::
				user.does("something else") ::
				user.call("call", system) ::
				system.reply("reply", user) :: Nil
		)

		val expected = load("complete-fixed-sequence.txt")

		val viewer = new FixedWidthViewer()
		viewer.view(flow.toEventBook) shouldBe expected
	}

	private def load(filename: String) = {
		Source.fromResource(filename).mkString
	}

}
