package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.MatrixGenerator.generate
import com.gnosly.fluentsequence.view.model._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class matrixGeneratorTest extends FlatSpec with Matchers {

	"generator" should "create matrix with DOES " in {
		val matrix = generate(EventBook(
			DONE(Actor(USER(), "user"), "something"),
			DONE(new Actor(USER(), "user"), "something else")
		))

		val matrixUserActor = new MatrixActor("user", Activity(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> matrixUserActor),
			List(AutoSignal("something", 0, matrixUserActor), AutoSignal("something else", 1, matrixUserActor))
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

		val matrixUserActor = new MatrixActor("user", Activity(0, 1))
		val matrixSystemActor = new MatrixActor("system", Activity(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(BiSignal("call", 0, matrixUserActor, matrixSystemActor), AutoSignal("something", 1, matrixSystemActor))
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

		val matrixUserActor = new MatrixActor("user", Activity(0, 2))
		val matrixSystemActor = new MatrixActor("system", Activity(0, 2))
		val expected = new Matrix().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(
				BiSignal("call", 0, matrixUserActor, matrixSystemActor),
				AutoSignal("something", 1, matrixSystemActor),
				BiSignal("response", 2, matrixSystemActor, matrixUserActor))
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

		val matrixUserActor = new MatrixActor("user", Activity(0, 4))
		val matrixSystemActor = new MatrixActor("system", Activity(1, 3))
		val expected = new Matrix().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(
				AutoSignal("something", 0, matrixUserActor),
				BiSignal("call", 1, matrixUserActor, matrixSystemActor),
				AutoSignal("something else", 2, matrixSystemActor),
				BiSignal("response", 3, matrixSystemActor, matrixUserActor),
				AutoSignal("something end", 4, matrixUserActor),
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
			REPLIED(systemActor, "response again", userActor),
		))

		val matrixUserActor = new MatrixActor("user", Activity(0, 5))
		val matrixSystemActor = MatrixActor("system", mutable.Buffer(Activity(0, 2), Activity(4, 5)))


		matrix shouldBe new Matrix().witha(
			Map("user" -> matrixUserActor, "system" -> matrixSystemActor),
			List(
				BiSignal("call", 0, matrixUserActor, matrixSystemActor),
				AutoSignal("something", 1, matrixSystemActor),
				BiSignal("response", 2, matrixSystemActor, matrixUserActor),
				AutoSignal("something end", 3, matrixUserActor),
				BiSignal("call again", 4, matrixUserActor, matrixSystemActor),
				BiSignal("response again", 5, matrixSystemActor, matrixUserActor),

			)
		)

	}


}
