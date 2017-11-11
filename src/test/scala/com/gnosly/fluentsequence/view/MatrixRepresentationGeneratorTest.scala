package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model._
import org.scalatest.{FlatSpec, Matchers}

class MatrixRepresentationGeneratorTest extends FlatSpec with Matchers {

	"ActorViewGenerator" should "transform" in {
		ActorViewGenerator.generate(EventBook(
			DONE(new Actor(USER(), "user"), "something")
		)) shouldBe List(ActorView("user"))
	}


	it should "distinct same actor" in {
		val sameActor = new Actor(USER(), "user")
		ActorViewGenerator.generate(EventBook(
			DONE(sameActor, "something"),
			NEW_SEQUENCE_SCHEDULED(sameActor, "sequence")
		)) shouldBe List(ActorView("user"))
	}



	ignore should "call own generators " in {
		val matrixRepresentation = MatrixRepresentationGenerator.generate(EventBook(
			DONE(new Actor(USER(), "user"), "something")
		))

		matrixRepresentation shouldBe new MatrixRepresentation(
			List(ActorView("user")),
			List(Activity(Step(1), TimelineStep(Step(1), Step(2)))),
			List(Signal("something", PointStep(Step(1), Step(1)), PointStep(Step(1), Step(2)))),
			List(Liveness(Step(1), Step(2)))
		)

	}

}
