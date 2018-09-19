package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.api.FluentSequence._
import com.gnosly.fluentsequence.core.{SEQUENCE_ENDED, _}
import org.scalatest.{FlatSpec, Matchers}

class FluentActorTest extends FlatSpec with Matchers {

  "actor" should "does something" in {
    val service = new FluentActor("service")

    val flow = service.does("something")

    flow.toEventBook shouldBe EventBook(DONE(service, "something"))
  }

  it should "start a new child sequence" in {
    val service = new FluentActor("service")

    val flow = service
      .does(Sequence("childSequence").startWith(service.does("something") :: Nil))
      .and()
      .does("something else")

    flow.toEventBook shouldBe EventBook(
      NEW_SEQUENCE_SCHEDULED(service, "childSequence"),
      SEQUENCE_STARTED("childSequence"),
      DONE(service, "something"),
      SEQUENCE_ENDED("childSequence"),
      DONE(service, "something else")
    )
  }

  it should "call another actor" in {
    val service = new FluentActor("service")
    val anotherActor = new FluentActor("anotherActor")

    val flow = service.call("action", anotherActor)

    flow.toEventBook shouldBe EventBook(
      CALLED(service, "action", anotherActor)
    )
  }

  it should "combine two actions" in {
    val service = new FluentActor("service")
    val anotherActor = new FluentActor("anotherActor")

    val flow = service
      .call("action", anotherActor)
      .and()
      .does("something")

    flow.toEventBook shouldBe EventBook(
      CALLED(service, "action", anotherActor),
      DONE(service, "something")
    )
  }

  it should "reply to another actor" in {
    val service = new FluentActor("service")
    val anotherService = new FluentActor("anotherService")

    val flow = service.reply("action", anotherService)

    flow.toEventBook shouldBe EventBook(
      REPLIED(service, "action", anotherService)
    )
  }

  "USER" should "does something" in {
    val user = new User("user")

    val flow = user.does("something")

    flow.toEventBook shouldBe EventBook(
      DONE(user, "something")
    )

  }

}
