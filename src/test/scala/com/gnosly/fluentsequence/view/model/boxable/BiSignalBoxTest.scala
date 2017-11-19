package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.model.BiSignal
import org.scalatest.{FunSuite, Matchers}

class BiSignalBoxTest extends FunSuite with Matchers {

	test("out") {
		val biSignalBox = BiSignalBox(BiSignal("something", 1, null, null))
		biSignalBox.out shouldBe
			"  something \n" +
				"----------->"
					.stripMargin
	}

	test("sizing"){
		val signalName = "something"
		val biSignalBox = BiSignalBox(BiSignal(signalName, 1, null, null))
		biSignalBox.minHeight()  shouldBe 2
		biSignalBox.minWidth() shouldBe signalName.length + 3
	}
}