package com.gnosly.fluentsequence.core

import org.scalatest.FunSuite
import org.scalatest.Matchers

class EventBookReaderTest extends FunSuite with Matchers {

  test("read encoded data") {
    val actor = Actor(USER_TYPE(), "actorName")
    val anotherActor = Actor(SEQUENCE_ACTOR_TYPE(), "anotherActorName")

    val reader = new EventBookReader

    val eventBook = reader.read("""SEQUENCE_STARTED|seqStarted
									|DONE|USER_TYPE|actorName|something
									|CALLED|USER_TYPE|actorName|call|SEQUENCE_ACTOR_TYPE|anotherActorName
									|FIRED|USER_TYPE|actorName|call async|SEQUENCE_ACTOR_TYPE|anotherActorName
									|REPLIED|USER_TYPE|actorName|reply|SEQUENCE_ACTOR_TYPE|anotherActorName
									|NEW_SEQUENCE_SCHEDULED|USER_TYPE|actorName|newSeqStarted
									|SEQUENCE_ENDED|seqEnded
									|ALTERNATIVE_STARTED|altStart
									|ALTERNATIVE_ENDED|altEnd""".stripMargin)

    val expectedEventBook = EventBook()
      .track(SEQUENCE_STARTED("seqStarted"))
      .track(DONE(actor, "something"))
      .track(CALLED(actor, "call", anotherActor))
      .track(FIRED(actor, "call async", anotherActor))
      .track(REPLIED(actor, "reply", anotherActor))
      .track(NEW_SEQUENCE_SCHEDULED(actor, "newSeqStarted"))
      .track(SEQUENCE_ENDED("seqEnded"))
      .track(ALTERNATIVE_STARTED("altStart"))
      .track(ALTERNATIVE_ENDED("altEnd"))

    eventBook shouldBe expectedEventBook
  }
}
