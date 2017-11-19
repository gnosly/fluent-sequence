package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.model.MatrixActor
import org.scalatest.{FunSuite, Matchers}

class ActorBoxTest extends FunSuite with Matchers {

	test("size"){
		val actorName = "name"
		val actorBox = ActorBox(MatrixActor(actorName,null))
		actorBox.minHeight() shouldBe 4
		actorBox.minWidth() shouldBe actorName.length + 4

	}
}
