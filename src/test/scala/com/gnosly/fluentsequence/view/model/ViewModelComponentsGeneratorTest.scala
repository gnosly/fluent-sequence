package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class ViewModelComponentsGeneratorTest extends FlatSpec with Matchers {

	val SYSTEM = Actor(SEQUENCE_ACTOR_TYPE(), "system")
	val USER = Actor(USER_TYPE(), "user")

	"generator" should "create view model with DOES " in {
		val viewModel = generate(EventBook(
			DONE(USER, "something"),
			DONE(USER, "something else")
		))

		val somethingSignal = new AutoSignalComponent("something", 0, 0, 0)
		val somethingElseSignal = new AutoSignalComponent("something else", 1, 0, 0)
		val rightPoints = mutable.TreeMap[Int, ActivityPoint](
			0 -> ActivityPoint(0, somethingSignal, true),
			1 -> ActivityPoint(1, somethingElseSignal, true)
		)
		val userComponent = new ActorComponent(0, "user",
			asBuffer(new ActivityComponent(0,  0,0, 1, rightPoints = rightPoints)))

		viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent))
	}


	it should "create viewModel with REPLY " in {
		val viewModel = generate(EventBook(
			CALLED(USER, "call", SYSTEM),
			REPLIED(SYSTEM, "response", USER)
		))


		val call = new BiSignalComponent("call", 0, 0, 0, 1, 0)
		val response = new BiSignalComponent("response", 1, 1, 0, 0, 0)

		val userRightPoints = mutable.TreeMap[Int, ActivityPoint](
			0 -> ActivityPoint(0, call, true),
			1 -> ActivityPoint(1, response, true)
		)

		val userComponent = new ActorComponent(0, "user",
			asBuffer(new ActivityComponent(0, 0,0, 1, rightPoints = userRightPoints)))

		val systemRightPoints = mutable.TreeMap[Int, ActivityPoint](
			0 -> ActivityPoint(0, call, false),
			1 -> ActivityPoint(1, response, false)
		)

		val systemComponent = new ActorComponent(1, "system",
			asBuffer(new ActivityComponent(0, 0,0, 1, leftPoints = systemRightPoints)))

		viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent, "system" -> systemComponent))
	}

	private def asBuffer(component: ActivityComponent): mutable.Buffer[ActivityComponent] = {
		mutable.Buffer[ActivityComponent](component)
	}
}
