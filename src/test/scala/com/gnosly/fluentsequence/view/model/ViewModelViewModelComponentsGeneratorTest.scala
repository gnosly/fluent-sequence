package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModelComponentsGenerator.generate
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class ViewModelViewModelComponentsGeneratorTest extends FlatSpec with Matchers {

	"generator" should "create matrix with DOES " in {
		val matrix = generate(EventBook(
			DONE(Actor(USER(), "user"), "something"),
			DONE(new Actor(USER(), "user"), "something else")
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val expected = new ViewModelComponents().witha(
			Map("user" -> matrixUserActor),
			List(AutoSignalComponent("something", 0, 0, matrixUserActor), AutoSignalComponent("something else", 1, 0, matrixUserActor))
		)
		matrix shouldBe expected
	}

	it should "create matrix with CALL " in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrix = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val matrixSystemActor = new ActorComponent(1, "system", ActivityComponent(0, 0, 1))
		val expected = new ViewModelComponents().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(BiSignalComponent("call", 0, matrixUserActor, matrixSystemActor), AutoSignalComponent("something", 1, 0, matrixSystemActor))
		)
		matrix shouldBe expected

	}

	it should "create matrix with REPLY " in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrix = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
			REPLIED(systemActor, "response", userActor)
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 2))
		val matrixSystemActor = new ActorComponent(1, "system", ActivityComponent(0, 0, 2))
		val expected = new ViewModelComponents().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(
				BiSignalComponent("call", 0, matrixUserActor, matrixSystemActor),
				AutoSignalComponent("something", 1, 0, matrixSystemActor),
				BiSignalComponent("response", 2, matrixSystemActor, matrixUserActor))
		)
		matrix shouldBe expected

	}

	it should "create matrix with mixed events" in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrix = generate(EventBook(
			DONE(userActor, "something"),
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something else"),
			REPLIED(systemActor, "response", userActor),
			DONE(userActor, "something end"),
		))

		val matrixUserActor = new ActorComponent(0,"user", ActivityComponent(0, 0, 4))
		val matrixSystemActor = new ActorComponent(1,"system", ActivityComponent(0, 1, 3))
		val expected = new ViewModelComponents().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(
				AutoSignalComponent("something", 0, 0, matrixUserActor),
				BiSignalComponent("call", 1, matrixUserActor, matrixSystemActor),
				AutoSignalComponent("something else", 2, 0, matrixSystemActor),
				BiSignalComponent("response", 3, matrixSystemActor,  matrixUserActor),
				AutoSignalComponent("something end", 4, 0, matrixUserActor),
			)
		)
		matrix shouldBe expected

	}

	it should "handle multiple activity" in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrix = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
			REPLIED(systemActor, "response", userActor),
			DONE(userActor, "something end"),
			CALLED(userActor, "call again", systemActor),
			REPLIED(systemActor, "response again", userActor)
		))

		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 5))
		val matrixSystemActor = ActorComponent(1, "system", mutable.Buffer(ActivityComponent(0, 0, 2), ActivityComponent(1, 4, 5)))


		matrix shouldBe new ViewModelComponents().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(
				BiSignalComponent("call", 0, matrixUserActor, matrixSystemActor),
				AutoSignalComponent("something", 1, 0, matrixSystemActor),
				BiSignalComponent("response", 2, matrixSystemActor, matrixUserActor),
				AutoSignalComponent("something end", 3, 0, matrixUserActor),
				BiSignalComponent("call again", 4, matrixUserActor, matrixSystemActor),
				BiSignalComponent("response again", 5, matrixSystemActor, matrixUserActor)
			)
		)

	}


}
