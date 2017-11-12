package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model._
import org.scalatest.{FlatSpec, Matchers}

class MatrixRepresentationGeneratorTest extends FlatSpec with Matchers {
	"generator" should "call own generators " in {
		val matrixRepresentation = MatrixRepresentationGenerator.generate(EventBook(
			DONE(new Actor(USER(), "user"), "something")
		))

		matrixRepresentation shouldBe new MatrixRepresentation(
			List(ActorView("user",Step(0))),
			List(Activity(Step(0), TimelineStep(Step(0), Step(1)))),
			List(Signal("something", PointStep(Step(0), Step(0)), PointStep(Step(0), Step(1)))),
			List(Liveness(Step(0), Step(1)))
		)

	}
}
