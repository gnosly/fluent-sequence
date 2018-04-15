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
			DONE(Actor(USER_TYPE(), "user"), "something"),
			DONE(new Actor(USER_TYPE(), "user"), "something else")
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val expected = ViewModelComponents(
			mutable.HashMap("user" -> matrixUserActor),
			mutable.Buffer(AutoSignalComponent("something", 0, matrixUserActor), AutoSignalComponent("something else", 1, matrixUserActor))
		)
		viewModel shouldBe expected
	}

	it should "create viewModel with CALL " in {
		val systemActor = Actor(SEQUENCE_ACTOR_TYPE(), "system")
		val userActor = new Actor(USER_TYPE(), "user")

		val viewModel = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val matrixSystemActor = new ActorComponent(1, "system", ActivityComponent(0, 0, 1))
		val expected = new ViewModelComponents(
			mutable.HashMap("user" -> matrixUserActor, "system" -> matrixSystemActor),
			mutable.Buffer(BiSignalComponent("call", 0, matrixUserActor, matrixSystemActor), AutoSignalComponent("something", 1, matrixSystemActor))
		)
		viewModel shouldBe expected

	}

	it should "create viewModel with REPLY " in {
		val viewModel = generate(EventBook(
			CALLED(USER, "call", SYSTEM),
			DONE(SYSTEM, "something"),
			REPLIED(SYSTEM, "response", USER)
		))

		val userComponent = new ActorComponent(0, "user", ActivityComponent(0, 0, 2))
		val systemComponent = new ActorComponent(1, "system", ActivityComponent(0, 0, 2))

		val expected = ViewModelComponents(
			mutable.HashMap("user" -> userComponent, "system" -> systemComponent),
			mutable.Buffer(
				BiSignalComponent("call", 0, userComponent, systemComponent),
				AutoSignalComponent("something", 1, systemComponent),
				BiSignalComponent("response", 2, systemComponent, userComponent))
		)

		viewModel shouldBe expected
	}

	it should "create viewModel with mixed events" in {
		val systemActor = Actor(SEQUENCE_ACTOR_TYPE(), "system")
		val userActor = new Actor(USER_TYPE(), "user")

		val viewModel = generate(EventBook(
			DONE(userActor, "something"),
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something else"),
			REPLIED(systemActor, "response", userActor),
			DONE(userActor, "something end"),
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 4))
		val matrixSystemActor = new ActorComponent(1, "system", ActivityComponent(0, 1, 3))
		val expected = new ViewModelComponents(
			mutable.HashMap("user" -> matrixUserActor, "system" -> matrixSystemActor),
			mutable.Buffer(
				AutoSignalComponent("something", 0, matrixUserActor),
				BiSignalComponent("call", 1, matrixUserActor, matrixSystemActor),
				AutoSignalComponent("something else", 2, matrixSystemActor),
				BiSignalComponent("response", 3, matrixSystemActor, matrixUserActor),
				AutoSignalComponent("something end", 4, matrixUserActor),
			)
		)
		viewModel shouldBe expected

	}

	it should "handle multiple activity" in {
		val systemActor = Actor(SEQUENCE_ACTOR_TYPE(), "system")
		val userActor = new Actor(USER_TYPE(), "user")

		val viewModel = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
			REPLIED(systemActor, "response", userActor),
			DONE(userActor, "something end"),
			CALLED(userActor, "call again", systemActor),
			REPLIED(systemActor, "response again", userActor)
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 5))
		val matrixSystemActor = ActorComponent(1, "system", mutable.Buffer(ActivityComponent(0, 0, 2), ActivityComponent(1, 4, 5)))


		viewModel shouldBe new ViewModelComponents(
			mutable.HashMap("user" -> matrixUserActor, "system" -> matrixSystemActor),
			mutable.Buffer(
				BiSignalComponent("call", 0, matrixUserActor, matrixSystemActor),
				AutoSignalComponent("something", 1, matrixSystemActor),
				BiSignalComponent("response", 2, matrixSystemActor, matrixUserActor),
				AutoSignalComponent("something end", 3, matrixUserActor),
				BiSignalComponent("call again", 4, matrixUserActor, matrixSystemActor),
				BiSignalComponent("response again", 5, matrixSystemActor, matrixUserActor)
			)
		)
	}
}
