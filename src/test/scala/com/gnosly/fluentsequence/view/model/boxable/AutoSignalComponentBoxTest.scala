package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.model.AutoSignalComponent
import org.scalatest.{FunSuite, Matchers}

class AutoSignalComponentBoxTest extends FunSuite with Matchers{

	test("sizing"){
		val signalName = "something"
		val autoSignalBox = AutoSignalBox(AutoSignalComponent(signalName,1,null))
		autoSignalBox.minHeight() shouldBe 4
		autoSignalBox.minWidth() shouldBe signalName.length + 6
	}
}
