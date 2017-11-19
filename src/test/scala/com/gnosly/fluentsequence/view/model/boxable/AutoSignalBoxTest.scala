package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.model.AutoSignal
import org.scalatest.{FunSuite, Matchers}

class AutoSignalBoxTest extends FunSuite with Matchers{

	test("sizing"){
		val signalName = "something"
		val autoSignalBox = AutoSignalBox(AutoSignal(signalName,1,null))
		autoSignalBox.minHeight() shouldBe 4
		autoSignalBox.minWidth() shouldBe signalName.length + 6
	}
}
