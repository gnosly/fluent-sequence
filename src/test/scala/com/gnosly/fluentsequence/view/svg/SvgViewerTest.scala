package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, User}
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class SvgViewerTest extends FunSuite with Matchers {
	val USER = new User("user")
	val SYSTEM = new FluentActor("system")
	val viewer = new SvgViewer()

	test("view an actor that calls another") {
		val flow = Sequence("example").startWith(
			USER.call("call", SYSTEM) :: Nil
		)

		val str = viewer.view(flow.toEventBook)
		println(str)
		str.print() shouldBe sequenceFromFile("two-actors-one-call.svg")
	}

	private def sequenceFromFile(filename: String) = {
		Source.fromResource(s"svg/$filename").mkString
	}
}
