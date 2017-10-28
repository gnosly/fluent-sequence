package com.gnosly.api

import com.gnosly.api.FluentSequence._
import org.scalatest.{FlatSpec, Matchers}

class ActorTest extends FlatSpec with Matchers {


	"actor" should "does something" in {
		val service = new Actor("service")

		val flow = service.does("something")

		flow.toEventBook shouldBe EventBook("ACTOR service DOES something")
	}

	it should "start a new child sequence" in {
		val service = new Actor("service")

		val flow = service
			.does(new Sequence("childSequence").startWith(service.does("something") :: Nil))
			.and()
			.does("something else")

		flow.toEventBook shouldBe EventBook(
			"ACTOR service STARTED_NEW_SEQUENCE childSequence",
			"SEQUENCE childSequence STARTED",
			"ACTOR service DOES something",
			"ACTOR service DOES something else")
	}

	it should "call another actor" in {
		val service = new Actor("service")

		val flow = service.call("action", new Actor("anotherActor"))

		flow.toEventBook shouldBe EventBook(
			"ACTOR service CALLED action TO anotherActor")
	}

	it should "combine two actions" in {
		val service = new Actor("service")

		val flow = service.call("action", new Actor("anotherActor"))
			.and()
			.does("something")

		flow.toEventBook shouldBe EventBook(
			"ACTOR service CALLED action TO anotherActor",
			"ACTOR service DOES something"
		)
	}


	it should "reply to another actor" in {
		val service = new Actor("service")
		val anotherService = new Actor("anotherService")

		val flow = service.reply("action", anotherService)

		flow.toEventBook shouldBe EventBook(
			"ACTOR service REPLIED action TO anotherService"
		)
	}

}
