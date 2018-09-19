package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.api.FluentSequence._
import com.gnosly.fluentsequence.core.{DONE, SEQUENCE_STARTED, _}
import org.scalatest.{FunSuite, Matchers}

class FluentSequenceTest extends FunSuite with Matchers {
  val user = new User("user")
  val system = new FluentActor("system")
  val anotherSystem = new FluentActor("anotherSystem")

  test("empty sequence") {
    FluentSequence.Sequence("sequenceName").toEventBook.toTimelineEventList shouldBe List()
  }

  test("user does something") {
    val sequence = Sequence("sequenceName").startWith(
      user.does("something") :: Nil
    )

    sequence.toEventBook shouldBe EventBook(
      SEQUENCE_STARTED("sequenceName"),
      DONE(user, "something"),
      SEQUENCE_ENDED("sequenceName")
    )
  }

  test("user interacts with a system") {
    val sequence = Sequence("sequenceName").startWith(
      user.call("call", system) ::
        system.reply("reply", user) :: Nil
    )

    sequence.toEventBook shouldBe EventBook(
      SEQUENCE_STARTED("sequenceName"),
      CALLED(user, "call", system),
      REPLIED(system, "reply", user),
      SEQUENCE_ENDED("sequenceName")
    )
  }

  test("user does multiple things") {
    val sequence = Sequence("sequenceName").startWith(
      user
        .does("a")
        .and()
        .does("b")
        .and()
        .call("call", system)
        .and()
        .reply("reply", anotherSystem)
        .and()
        .does(Sequence("another sequence").startWith(user.does("c") :: Nil))
        :: Nil
    )

    sequence.toEventBook shouldBe EventBook(
      SEQUENCE_STARTED("sequenceName"),
      DONE(user, "a"),
      DONE(user, "b"),
      CALLED(user, "call", system),
      REPLIED(user, "reply", anotherSystem),
      NEW_SEQUENCE_SCHEDULED(user, "another sequence"),
      SEQUENCE_STARTED("another sequence"),
      DONE(user, "c"),
      SEQUENCE_ENDED("another sequence"),
      SEQUENCE_ENDED("sequenceName")
    )
  }
}
