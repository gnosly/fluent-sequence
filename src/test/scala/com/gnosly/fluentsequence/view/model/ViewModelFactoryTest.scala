package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModelFactory.viewModelFrom
import com.gnosly.fluentsequence.view.model.ViewModels._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ViewModelFactoryTest extends FlatSpec with Matchers {

  val SYSTEM = Actor(SEQUENCE_ACTOR_TYPE(), "system")
  val USER = Actor(USER_TYPE(), "user")

  "generator" should "create view model with DOES " in {
    val viewModel = viewModelFrom(
      EventBook(
        DONE(USER, "something"),
        DONE(USER, "something else")
      ))

    val rights = List(PointOnTheRight(0, AutoSignalModel("something", 0, 0, 0)),
                      PointOnTheRight(1, AutoSignalModel("something else", 1, 0, 0)))

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", true, List(ActivityModel(0, 0, 0, 1, rights)))),
      List(),
      List(),
      1
    )
  }

  it should "create viewModel with REPLY " in {
    val viewModel = viewModelFrom(
      EventBook(
        CALLED(USER, "call", SYSTEM),
        REPLIED(SYSTEM, "response", USER)
      ))

    val call = new SyncRequest("call", 0, 0, 0, 1, 0)
    val response = new SyncResponse("response", 1, 1, 0, 0, 0)

    val userPoints = List[PointOnTheRight](
      PointOnTheRight(0, call),
      PointOnTheRight(1, response)
    )

    val systemPoints = List[PointOnTheLeft](
      PointOnTheLeft(0, call),
      PointOnTheLeft(1, response)
    )

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", false, List(ActivityModel(0, 0, 0, 1, userPoints))),
           ActorModel(1, "system", true, List(ActivityModel(0, 1, 0, 1, List(), systemPoints)))),
      List(),
      List(),
      1
    )
  }

  it should "create viewModel with FIRE " in {
    val viewModel = viewModelFrom(
      EventBook(
        FIRED(USER, "call", SYSTEM)
      ))

    val call = new AsyncRequest("call", 0, 0, 0, 1, 0)

    val userPoints = List[PointOnTheRight](PointOnTheRight(0, call))

    val systemPoints = List[PointOnTheLeft](
      PointOnTheLeft(0, call)
    )

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", false, List(ActivityModel(0, 0, 0, 0, userPoints))),
           ActorModel(1, "system", true, List(ActivityModel(0, 1, 0, 0, List(), systemPoints)))),
      List(),
      List(),
      0
    )
  }

  it should "alternative " in {
    val viewModel = viewModelFrom(
      EventBook(
        DONE(USER, "something"),
        ALTERNATIVE_STARTED("condition"),
        DONE(USER, "something else"),
        ALTERNATIVE_ENDED("condition")
      ))

    viewModel.alternatives contains AlternativeModel(0, "condition", 1, 3)
  }

  it should "count signal index" in {
    val viewModel = viewModelFrom(
      EventBook(
        SEQUENCE_STARTED("sequenceName"),
        DONE(USER, "signalA"),
        CALLED(USER, "signalB", SYSTEM),
        REPLIED(SYSTEM, "signalC", USER),
        SEQUENCE_STARTED("another sequence"),
        DONE(USER, "signalD"),
        SEQUENCE_ENDED("another sequence"),
        DONE(USER, "signalE"),
        SEQUENCE_ENDED("sequenceName")
      ))

    val signalA = AutoSignalModel("signalA", 1, 0, 0)
    val signalB = new SyncRequest("signalB", 2, 0, 0, 1, 0)
    val signalC = new SyncResponse("signalC", 3, 1, 0, 0, 0)
    val signalD = AutoSignalModel("signalD", 5, 0, 0)
    val signalE = AutoSignalModel("signalE", 7, 0, 0)

    val userPoints = List[PointOnTheRight](
      PointOnTheRight(1, signalA),
      PointOnTheRight(2, signalB),
      PointOnTheRight(3, signalC),
      PointOnTheRight(5, signalD),
      PointOnTheRight(7, signalE)
    )

    val systemPoints = List[PointOnTheLeft](
      PointOnTheLeft(2, signalB),
      PointOnTheLeft(3, signalC)
    )

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", false, List(ActivityModel(0, 0, 1, 7, userPoints))),
           ActorModel(1, "system", true, List(ActivityModel(0, 1, 2, 3, List(), systemPoints)))),
      List(SequenceModel("sequenceName", 0), SequenceModel("another sequence", 4)),
      List(),
      7
    )
  }
}
