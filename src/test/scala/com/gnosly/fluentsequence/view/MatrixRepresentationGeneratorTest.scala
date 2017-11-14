package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.MatrixGenerator.generate
import com.gnosly.fluentsequence.view.model._
import org.scalatest.{FlatSpec, Matchers}

class MatrixRepresentationGeneratorTest extends FlatSpec with Matchers {

	"generator" should "create matrix with DOES " in {
		val matrixRepresentation = generate(EventBook(
			DONE(Actor(USER(), "user"), "something"),
			DONE(new Actor(USER(), "user"), "something else")
		))

		val userActor2 = MatrixActor("user", Activity(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> userActor2),
			List(AutoSignal("something", 0, userActor2), AutoSignal("something else", 1, userActor2))
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with CALL " in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrixRepresentation = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
		))

		val userActor2 = MatrixActor("user", Activity(0, 1))
		val systemActor2 = MatrixActor("system", Activity(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(BiSignal("call", 0, userActor2, systemActor2), AutoSignal("something", 1, systemActor2))
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with REPLY " in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrixRepresentation = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
			REPLIED(systemActor, "response", userActor)
		))

		val userActor2 = MatrixActor("user", Activity(0, 2))
		val systemActor2 = MatrixActor("system", Activity(0, 2))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(
				BiSignal("call", 0, userActor2, systemActor2),
				AutoSignal("something", 1, systemActor2),
				BiSignal("response", 2, systemActor2, userActor2))
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with mixed events" in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrixRepresentation = generate(EventBook(
			DONE(userActor, "something"),
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something else"),
			REPLIED(systemActor, "response", userActor),
			DONE(userActor, "something end"),
		))

		val userActor2 = MatrixActor("user", Activity(0, 4))
		val systemActor2 = MatrixActor("system", Activity(1, 3))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(
				AutoSignal("something", 0, userActor2),
				BiSignal("call", 1, userActor2, systemActor2),
				AutoSignal("something else", 2, systemActor2),
				BiSignal("response", 3, systemActor2, userActor2),
				AutoSignal("something end", 4, userActor2),
			)
		)
		matrixRepresentation shouldBe expected

	}

	it should "handle multiple activity" in {

	}


}
