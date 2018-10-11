package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory.viewModelFrom
import com.gnosly.fluentsequence.view.model.component._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.collection.mutable

class ViewModelComponentsFactoryTest extends FlatSpec with Matchers {

  val SYSTEM = Actor(SEQUENCE_ACTOR_TYPE(), "system")
  val USER = Actor(USER_TYPE(), "user")

  "generator" should "create view model with DOES " in {
    val viewModel = viewModelFrom(
      EventBook(
        DONE(USER, "something"),
        DONE(USER, "something else")
      ))

    viewModel shouldBe ViewModelComponents(
      mutable.HashMap("user" -> new ActorComponent(
        0,
        "user",
        asBuffer(new ActivityComponent(
          0,
          0,
          0,
          1,
          _rightPoints = mutable.ListBuffer[ActivityPointOnTheRight](
            ActivityPointOnTheRight(0, new AutoSignalComponent("something", 0, 0, 0)),
            ActivityPointOnTheRight(1, new AutoSignalComponent("something else", 1, 0, 0))
          )
        )),
        true
      )))
  }

  it should "create viewModel with REPLY " in {
    val viewModel = viewModelFrom(
      EventBook(
        CALLED(USER, "call", SYSTEM),
        REPLIED(SYSTEM, "response", USER)
      ))

    val call = new SyncRequest("call", 0, 0, 0, 1, 0)
    val response = new SyncResponse("response", 1, 1, 0, 0, 0)

    val userRightPoints = mutable.ListBuffer[ActivityPointOnTheRight](
      ActivityPointOnTheRight(0, call),
      ActivityPointOnTheRight(1, response)
    )

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 1, _rightPoints = userRightPoints)))

    val systemRightPoints = mutable.ListBuffer[ActivityPointOnTheLeft](
      ActivityPointOnTheLeft(0, call),
      ActivityPointOnTheLeft(1, response)
    )

    val systemComponent =
      new ActorComponent(1,
                         "system",
                         asBuffer(new ActivityComponent(0, 0, 0, 1, _leftPoints = systemRightPoints)),
                         true)

    viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent, "system" -> systemComponent))
  }

  it should "create viewModel with FIRE " in {
    val viewModel = viewModelFrom(
      EventBook(
        FIRED(USER, "call", SYSTEM)
      ))

    val call = new AsyncRequest("call", 0, 0, 0, 1, 0)

    val userRightPoints = mutable.ListBuffer[ActivityPointOnTheRight](ActivityPointOnTheRight(0, call))

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 0, _rightPoints = userRightPoints)))

    val systemRightPoints = mutable.ListBuffer[ActivityPointOnTheLeft](
      ActivityPointOnTheLeft(0, call)
    )

    val systemComponent =
      new ActorComponent(1,
                         "system",
                         asBuffer(new ActivityComponent(0, 0, 0, 0, _leftPoints = systemRightPoints)),
                         true)

    viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent, "system" -> systemComponent))
  }

  it should "alternative " in {
    val viewModel = viewModelFrom(
      EventBook(
        DONE(USER, "something"),
        ALTERNATIVE_STARTED("condition"),
        DONE(USER, "something else"),
        ALTERNATIVE_ENDED("condition")
      ))

    viewModel.alternatives shouldBe mutable.ListBuffer(AlternativeComponent(0, "condition", 0, 1))
  }

  it should "count signal index" in {
    val viewModel = viewModelFrom(
      EventBook(
        SEQUENCE_STARTED("sequenceName"),
        DONE(USER, "signalA"),
        CALLED(USER, "signalB", SYSTEM),
        REPLIED(SYSTEM, "signalC", USER),
        NEW_SEQUENCE_SCHEDULED(USER, "another sequence"),
        SEQUENCE_STARTED("another sequence"),
        DONE(USER, "signalD"),
        SEQUENCE_ENDED("another sequence"),
        DONE(USER, "signalE"),
        SEQUENCE_ENDED("sequenceName")
      ))

    val signalA = new AutoSignalComponent("signalA", 0, 0, 0)
    val signalB = new SyncRequest("signalB", 1, 0, 0, 1, 0)
    val signalC = new SyncResponse("signalC", 2, 1, 0, 0, 0)
    val signalD = new AutoSignalComponent("signalD", 3, 0, 0)
    val signalE = new AutoSignalComponent("signalE", 4, 0, 0)

    val userRightPoints = mutable.ListBuffer[ActivityPointOnTheRight](
      ActivityPointOnTheRight(0, signalA),
      ActivityPointOnTheRight(1, signalB),
      ActivityPointOnTheRight(2, signalC),
      ActivityPointOnTheRight(3, signalD),
      ActivityPointOnTheRight(4, signalE)
    )

    val systemLeftPoints = mutable.ListBuffer[ActivityPointOnTheLeft](
      ActivityPointOnTheLeft(1, signalB),
      ActivityPointOnTheLeft(2, signalC)
    )

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 4, _rightPoints = userRightPoints)), false)

    val systemComponent =
      new ActorComponent(1, "system", asBuffer(new ActivityComponent(0, 0, 1, 2, _leftPoints = systemLeftPoints)), true)

    viewModel shouldBe ViewModelComponents(
      mutable.HashMap("user" -> userComponent, "system" -> systemComponent),
      mutable.ListBuffer(new SequenceComponent("sequenceName", -1), new SequenceComponent("another sequence", 2))
    )
  }

  private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
    mutable.Buffer[ActivityComponent](component)
  }
}
