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



		val somethingSignal = new AutoSignalComponent("something", 0, 0)
		val somethingElseSignal = new AutoSignalComponent("something else", 1, 0)
		val rightPoints = mutable.TreeMap[Int, ActivityPoint](
			0 -> ActivityPoint(0, somethingSignal, "right"),
			1 -> ActivityPoint(1, somethingElseSignal, "right")
		)
		val userComponent = new ActorComponent(0, "user",
			new ActivityComponent(0, 0, 1, rightPoints = rightPoints))

		viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent))
	}

	it should "create viewModel with REPLY " in {
		val viewModel = generate(EventBook(
			CALLED(USER, "call", SYSTEM),
			REPLIED(SYSTEM, "response", USER)
		))


		val call = new BiSignalComponent("call", 0, 0, 1)
		val response = new BiSignalComponent("response", 1, 1, 0)

		val userRightPoints = mutable.TreeMap[Int, ActivityPoint](
			0 -> ActivityPoint(0, call, "right"),
			1 -> ActivityPoint(1, response, "right")
		)

		val userComponent = new ActorComponent(0, "user",
			new ActivityComponent(0, 0, 1, rightPoints = userRightPoints))

		val systemRightPoints = mutable.TreeMap[Int, ActivityPoint](
			0 -> ActivityPoint(0, call, "left"),
			1 -> ActivityPoint(1, response, "left")
		)

		val systemComponent = new ActorComponent(1, "system",
			new ActivityComponent(0, 0, 1, leftPoints = systemRightPoints))

		viewModel shouldBe ViewModelComponents(mutable.HashMap("user" -> userComponent, "system" -> systemComponent))


	}

//	it should "create viewModel with mixed events" in {
//
//		val viewModel = generate(EventBook(
//			DONE(USER, "something"),
//			CALLED(USER, "call", SYSTEM),
//			DONE(SYSTEM, "something else"),
//			REPLIED(SYSTEM, "response", USER),
//			DONE(USER, "something end")
//		))
//
//		val matrixUserActor = new ActorComponent(0, "user", new ActivityComponent(0, 0, 4))
//		val matrixSystemActor = new ActorComponent(1, "system", new ActivityComponent(0, 1, 3))
//		val something = new AutoSignalComponent("something", 0, 0)
//		val call = new BiSignalComponent("call", 1, matrixUserActor, matrixSystemActor)
//		val somethingElse = new AutoSignalComponent("something else", 2, 1)
//		val response = new BiSignalComponent("response", 3, matrixSystemActor, matrixUserActor)
//		val somethingEnd = new AutoSignalComponent("something end", 4, 0)
//		val expected = ViewModelComponents(
//			mutable.HashMap("user" -> matrixUserActor, "system" -> matrixSystemActor)
//		)
//		viewModel shouldBe expected
//
//	}
	//
	//	it should "handle multiple activity" in {
	//		val viewModel = generate(EventBook(
	//			CALLED(USER, "call", SYSTEM),
	//			DONE(SYSTEM, "something"),
	//			REPLIED(SYSTEM, "response", USER),
	//			DONE(USER, "something end"),
	//			CALLED(USER, "call again", SYSTEM),
	//			REPLIED(SYSTEM, "response again", USER)
	//		))
	//
	//		val matrixUserActor = new ActorComponent(0, "user", new ActivityComponent(0, 0, 5))
	//		val matrixSystemActor = new ActorComponent(1, "system", mutable.Buffer(new ActivityComponent(0, 0, 2), new ActivityComponent(1, 4, 5)))
	//
	//
	//		viewModel shouldBe ViewModelComponents(
	//			mutable.HashMap("user" -> matrixUserActor, "system" -> matrixSystemActor),
	//			mutable.Buffer(
	//				new BiSignalComponent("call", 0, matrixUserActor, matrixSystemActor),
	//				new AutoSignalComponent("something", 1, 1),
	//				new BiSignalComponent("response", 2, matrixSystemActor, matrixUserActor),
	//				new AutoSignalComponent("something end", 3, 0),
	//				new BiSignalComponent("call again", 4, matrixUserActor, matrixSystemActor),
	//				new BiSignalComponent("response again", 5, matrixSystemActor, matrixUserActor)
	//			)
	//		)
	//	}
}
