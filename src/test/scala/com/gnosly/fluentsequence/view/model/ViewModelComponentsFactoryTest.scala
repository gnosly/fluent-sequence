package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory.createFrom
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class ViewModelComponentsFactoryTest extends FlatSpec with Matchers {

	val SYSTEM = Actor(SEQUENCE_ACTOR_TYPE(), "system")
	val USER = Actor(USER_TYPE(), "user")

	"generator" should "create view model with DOES " in {
		val viewModel = createFrom(EventBook(
			DONE(USER, "something"),
			DONE(USER, "something else")
		))

		val somethingSignal = new AutoSignalComponent("something", 0, 0, 0)
		val somethingElseSignal = new AutoSignalComponent("something else", 1, 0, 0)
		val rightPoints = mutable.TreeMap[Int, RightPoint](
			0 -> ActivityPointLoopOnTheRight(0, somethingSignal),
			1 -> ActivityPointLoopOnTheRight(1, somethingElseSignal)
		)
		val userComponent = new ActorComponent(0, "user",
			asBuffer(new ActivityComponent(0,  0,0, 1, rightPoints = rightPoints)))

		viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent))
	}

	it should "create viewModel with REPLY " in {
		val viewModel = createFrom(EventBook(
			CALLED(USER, "call", SYSTEM),
			REPLIED(SYSTEM, "response", USER)
		))

		val call = new BiSignalComponent("call", 0, 0, 0, 1, 0)
		val response = new BiSignalComponent("response", 1, 1, 0, 0, 0)

		val userRightPoints = mutable.TreeMap[Int, RightPoint](
			0 -> ActivityPointForBiSignalOnTheRight(0, call),
			1 -> ActivityPointForBiSignalOnTheRight(1, response)
		)

		val userComponent = new ActorComponent(0, "user",
			asBuffer(new ActivityComponent(0, 0,0, 1, rightPoints = userRightPoints)))

		val systemRightPoints = mutable.TreeMap[Int, LeftPoint](
			0 -> ActivityPointForBiSignalOnTheLeft(0, call),
			1 -> ActivityPointForBiSignalOnTheLeft(1, response)
		)

		val systemComponent = new ActorComponent(1, "system",
			asBuffer(new ActivityComponent(0, 0,0, 1, leftPoints = systemRightPoints)))

		viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent, "system" -> systemComponent))
	}

	it should "generate start" in {
		val viewModel = createFrom(EventBook(
			SEQUENCE_STARTED("sequence name"),
			SEQUENCE_STARTED("another sub sequence name")
		))

		viewModel shouldBe ViewModelComponents(sequenceComponents = mutable.ListBuffer(
			new SequenceComponent("sequence name",0),
			new SequenceComponent("another sub sequence name",1)))
	}

	private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
		mutable.Buffer[ActivityComponent](component)
	}
}
