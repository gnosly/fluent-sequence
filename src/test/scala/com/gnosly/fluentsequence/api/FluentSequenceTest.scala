package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.api.FluentSequence._
import com.gnosly.fluentsequence.core.DONE
import com.gnosly.fluentsequence.core.SEQUENCE_STARTED
import com.gnosly.fluentsequence.core._
import org.scalatest.FunSuite
import org.scalatest.Matchers

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
      SEQUENCE_STARTED("another sequence"),
      DONE(user, "c"),
      SEQUENCE_ENDED("another sequence"),
      SEQUENCE_ENDED("sequenceName")
    )
  }

  test("user tell something to someone") {
    val sequence = Sequence("sequenceName").startWith(
      user.fire("something", system).and().fire("something else", anotherSystem) :: Nil
    )

    val book = EventBook(
      SEQUENCE_STARTED("sequenceName"),
      FIRED(user, "something", system),
      FIRED(user, "something else", anotherSystem),
      SEQUENCE_ENDED("sequenceName")
    )
    sequence.toEventBook shouldBe book
  }

  test("user if want does something otherwise nothing") {
    val sequence = Sequence("sequenceName").startWith(
      inCase("he/she wants", user.does("something") :: Nil) ::
        inCase("otherwise", user.does("nothing") :: Nil) :: Nil
    )

    val book = EventBook(
      SEQUENCE_STARTED("sequenceName"),
      ALTERNATIVE_STARTED("he/she wants"),
      DONE(user, "something"),
      ALTERNATIVE_ENDED("he/she wants"),
      ALTERNATIVE_STARTED("otherwise"),
      DONE(user, "nothing"),
      ALTERNATIVE_ENDED("otherwise"),
      SEQUENCE_ENDED("sequenceName")
    )
    sequence.toEventBook shouldBe book
  }
}
