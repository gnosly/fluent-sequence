package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.{Activity, AutoSignal, Matrix, MatrixActor}
import org.scalatest.{FlatSpec, Matchers}

class FixedWidthCanvasTest extends FlatSpec with Matchers {

	it should "render user" in {
		val matrixUserActor = new MatrixActor("user", Activity(0, 1))

		ActorBox(matrixUserActor).out shouldBe
			".------.\n" +
				"| user |\n" +
				"'------'\n" +
				"   |   \n"
	}

	it should "render autosignal" in {
		val matrixUserActor = new MatrixActor("user", Activity(0, 1))

		val box = AutoSignalBox(new AutoSignal("something", 1, matrixUserActor))

		box.out shouldBe
			"""____
				|		 |
				|    | something
				|<---'""".stripMargin

		box.width() shouldBe 15
		box.height() shouldBe 4
	}

	it should "render bisignal" in {
		val matrixUserActor = new MatrixActor("user", Activity(0, 1))

		val box = AutoSignalBox(new AutoSignal("something", 1, matrixUserActor))

		box.out shouldBe
			"""____
				|		 |
				|    | something
				|<---'""".stripMargin

		box.width() shouldBe 15
		box.height() shouldBe 4
	}

	it should "render entire autosignal" ignore {
		val matrixUserActor = new MatrixActor("user", Activity(0, 1))
		val matrix = new Matrix().witha(
			Map("user" -> matrixUserActor),
			List(
				AutoSignal("something", 0, matrixUserActor)
			)
		)

		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.out shouldBe
			"""    .------.
				|  	 | user |
				|  	 '------'
				|  			 |
				|       _|_
				|       | |____
				|       | |		 |
				|       | |    | something
				|       | |<---'
				|       |_|
				|        |
				|"""
	}

	it should "render multiple autosignal" ignore {
		val matrixUserActor = new MatrixActor("user", Activity(0, 1))
		val matrix = new Matrix().witha(
			Map("user" -> matrixUserActor),
			List(
				AutoSignal("something", 0, matrixUserActor),
				AutoSignal("something else", 1, matrixUserActor)
			)
		)

		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.out shouldBe
			"""    .------.
				|  	 | user |
				|  	 '------'
				|  			 |
				|       _|_
				|       | |____
				|       | |		 |
				|       | |    | something
				|       | |<---'
				|       | |
				|  			| |____
				|       | |		 |
				|       | |    | something else
				|       | |<---'
				|       | |
				|       |_|
				|        |
				|"""
	}


}
