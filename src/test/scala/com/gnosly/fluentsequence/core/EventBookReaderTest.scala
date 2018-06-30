package com.gnosly.fluentsequence.core

import org.scalatest.{FunSuite, Matchers}

class EventBookReaderTest extends FunSuite with Matchers {

	test("read encoded data"){
		val actor = new Actor(USER_TYPE(), "actorName")
		val anotherActor = new Actor(SEQUENCE_ACTOR_TYPE(), "anotherActorName")

		val reader = new EventBookReader()

		val eventBook = reader.read("""SEQUENCE_STARTED|seqStarted
									|DONE|USER_TYPE|actorName|something
									|CALLED|USER_TYPE|actorName|call|SEQUENCE_ACTOR_TYPE|anotherActorName
									|REPLIED|USER_TYPE|actorName|reply|SEQUENCE_ACTOR_TYPE|anotherActorName
									|NEW_SEQUENCE_SCHEDULED|USER_TYPE|actorName|newSeqStarted
									|SEQUENCE_ENDED|seqEnded""".stripMargin)

		val expectedEventBook = new EventBook()
		expectedEventBook.track(SEQUENCE_STARTED("seqStarted"))
		expectedEventBook.track(DONE(actor, "something"))
		expectedEventBook.track(CALLED(actor, "call", anotherActor))
		expectedEventBook.track(REPLIED(actor, "reply", anotherActor))
		expectedEventBook.track(NEW_SEQUENCE_SCHEDULED(actor, "newSeqStarted"))
		expectedEventBook.track(SEQUENCE_ENDED("seqEnded"))

		eventBook shouldBe expectedEventBook
	}
}
