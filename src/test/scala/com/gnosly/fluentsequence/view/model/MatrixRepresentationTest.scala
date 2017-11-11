package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core.{Actor, DONE, EventBook, USER}
import org.scalatest.{FlatSpec, Matchers}

class MatrixRepresentationTest extends FlatSpec with Matchers {

//	"Matrix" should "be created by EventBook" in {
//
//		val matrixRepresentation = new MatrixRepresentation(EventBook(
//			DONE(new Actor(USER(), "user"), "something")
//		))
//
//		matrixRepresentation shouldBe new MatrixRepresentation(
//			List(ActorView("user")),
//			List(Activity(Step(1),TimelineStep(Step(1),Step(2)))),
//			List(Signal("something",PointStep(Step(1),Step(1)),PointStep(Step(1),Step(2)))),
//			List(Liveness(Step(1),Step(2)))
//		)
//
//	}
}
