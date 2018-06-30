package com.gnosly.fluentsequence.core

import org.scalatest.{FunSuite, Matchers}

class EventBookWriterTest extends FunSuite with Matchers{

	test("write encoded data"){
		val actor = new Actor(USER_TYPE(), "actorName")
		val anotherActor = new Actor(SEQUENCE_ACTOR_TYPE(), "anotherActorName")

		val book = new EventBook()
		book.track(SEQUENCE_STARTED("seqStarted"))
		book.track(DONE(actor, "something"))
		book.track(CALLED(actor, "call", anotherActor))
		book.track(REPLIED(actor, "reply", anotherActor))
		book.track(NEW_SEQUENCE_SCHEDULED(actor, "newSeqStarted"))
		book.track(SEQUENCE_ENDED("seqEnded"))

		new EventBookWriter().write(book) shouldBe
			"""SEQUENCE_STARTED|seqStarted
				|DONE|USER_TYPE|actorName|something
				|CALLED|USER_TYPE|actorName|call|SEQUENCE_ACTOR_TYPE|anotherActorName
				|REPLIED|USER_TYPE|actorName|reply|SEQUENCE_ACTOR_TYPE|anotherActorName
				|NEW_SEQUENCE_SCHEDULED|USER_TYPE|actorName|newSeqStarted
				|SEQUENCE_ENDED|seqEnded""".stripMargin
	}

}
