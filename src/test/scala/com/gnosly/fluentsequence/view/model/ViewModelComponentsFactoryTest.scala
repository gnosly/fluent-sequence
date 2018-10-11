package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory.viewModelFrom
import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
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

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", true)),
      List(ActivityModel(0, 0, 0, 1)),
      List(PointOnTheRight(0, AutoSignalModel("something", 0, 0, 0)),
           PointOnTheRight(1, AutoSignalModel("something else", 1, 0, 0))),
      List(
        new ActorComponent(
          0,
          "user",
          asBuffer(new ActivityComponent(
            0,
            0,
            0,
            1,
            _rightPoints = mutable.ListBuffer[PointOnTheRight](
              PointOnTheRight(0, AutoSignalModel("something", 0, 0, 0)),
              PointOnTheRight(1, AutoSignalModel("something else", 1, 0, 0))
            )
          )),
          true
        )),
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

    val userPoints = mutable.ListBuffer[PointOnTheRight](
      PointOnTheRight(0, call),
      PointOnTheRight(1, response)
    )

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 1, _rightPoints = userPoints)))

    val systemPoints = mutable.ListBuffer[PointOnTheLeft](
      PointOnTheLeft(0, call),
      PointOnTheLeft(1, response)
    )

    val systemComponent =
      new ActorComponent(1, "system", asBuffer(new ActivityComponent(0, 0, 0, 1, _leftPoints = systemPoints)), true)

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", false), ActorModel(1, "system", true)),
      List(ActivityModel(0, 0, 0, 1), ActivityModel(0, 1, 0, 1)),
      userPoints.toList ++ systemPoints.toList,
      List(userComponent, systemComponent),
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

    val userPoints = mutable.ListBuffer[PointOnTheRight](PointOnTheRight(0, call))

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 0, _rightPoints = userPoints)))

    val systemPoints = mutable.ListBuffer[PointOnTheLeft](
      PointOnTheLeft(0, call)
    )

    val systemComponent =
      new ActorComponent(1, "system", asBuffer(new ActivityComponent(0, 0, 0, 0, _leftPoints = systemPoints)), true)

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", false), ActorModel(1, "system", true)),
      List(ActivityModel(0, 0, 0, 0), ActivityModel(0, 1, 0, 0)),
      userPoints.toList ++ systemPoints.toList,
      List(userComponent, systemComponent),
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

    val signalA = AutoSignalModel("signalA", 0, 0, 0)
    val signalB = new SyncRequest("signalB", 1, 0, 0, 1, 0)
    val signalC = new SyncResponse("signalC", 2, 1, 0, 0, 0)
    val signalD = AutoSignalModel("signalD", 3, 0, 0)
    val signalE = AutoSignalModel("signalE", 4, 0, 0)

    val userPoints = mutable.ListBuffer[PointOnTheRight](
      PointOnTheRight(0, signalA),
      PointOnTheRight(1, signalB),
      PointOnTheRight(2, signalC),
      PointOnTheRight(3, signalD),
      PointOnTheRight(4, signalE)
    )

    val systemPoints = mutable.ListBuffer[PointOnTheLeft](
      PointOnTheLeft(1, signalB),
      PointOnTheLeft(2, signalC)
    )

    val userComponent =
      new ActorComponent(0, "user", asBuffer(new ActivityComponent(0, 0, 0, 4, _rightPoints = userPoints)), false)

    val systemComponent =
      new ActorComponent(1, "system", asBuffer(new ActivityComponent(0, 0, 1, 2, _leftPoints = systemPoints)), true)

    viewModel shouldBe ViewModel(
      List(ActorModel(0, "user", false), ActorModel(1, "system", true)),
      List(ActivityModel(0, 0, 0, 4), ActivityModel(0, 1, 1, 2)),
      userPoints.toList ++ systemPoints.toList,
      List(userComponent, systemComponent),
      List(new SequenceModel("sequenceName", -1), SequenceModel("another sequence", 2)),
      List(),
      4
    )
  }

  private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
    mutable.Buffer[ActivityComponent](component)
  }
}
