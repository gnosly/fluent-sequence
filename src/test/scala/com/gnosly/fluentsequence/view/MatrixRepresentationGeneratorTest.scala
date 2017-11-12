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
			List(ActorView("user",0)),
			List(Activity(0, TimelineStep(0, 1))),
			List(Signal("something", PointStep(0, 0), PointStep(0, 1))),
			List(Liveness(0, 1))
		)

	}
}
