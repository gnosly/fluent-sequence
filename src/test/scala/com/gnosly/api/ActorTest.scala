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

		val flow = service.does(new Sequence("childSequence").startWith(
			service.does("something") :: Nil
		))

		flow.toEventBook shouldBe EventBook(
			"ACTOR service STARTED_NEW_SEQUENCE childSequence",
			"SEQUENCE childSequence STARTED",
			"ACTOR service DOES something")
	}

}
