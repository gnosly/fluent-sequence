package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class UtilTest extends FunSuite with Matchers{

	test("middle"){
		Util.middle("x", 10) shouldBe "     x     "
		Util.middle("x", 11) shouldBe "     x     "
	}

}
