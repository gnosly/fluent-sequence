package com.gnosly.fluentsequence.core

import org.scalatest.{FunSuite, Matchers}

class EventBookWriterTest extends FunSuite with Matchers {

  test("write encoded data") {
    val actor = Actor(USER_TYPE(), "actorName")
    val anotherActor = Actor(SEQUENCE_ACTOR_TYPE(), "anotherActorName")

    val book = EventBook()
      .track(SEQUENCE_STARTED("seqStarted"))
      .track(DONE(actor, "something"))
      .track(CALLED(actor, "call", anotherActor))
      .track(REPLIED(actor, "reply", anotherActor))
      .track(NEW_SEQUENCE_SCHEDULED(actor, "newSeqStarted"))
      .track(SEQUENCE_ENDED("seqEnded"))

    new EventBookWriter().write(book) shouldBe
      """SEQUENCE_STARTED|seqStarted
				|DONE|USER_TYPE|actorName|something
				|CALLED|USER_TYPE|actorName|call|SEQUENCE_ACTOR_TYPE|anotherActorName
				|REPLIED|USER_TYPE|actorName|reply|SEQUENCE_ACTOR_TYPE|anotherActorName
				|NEW_SEQUENCE_SCHEDULED|USER_TYPE|actorName|newSeqStarted
				|SEQUENCE_ENDED|seqEnded""".stripMargin
  }

}
