package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FlatSpec, Matchers}

class FixedWidthCanvasTest extends FlatSpec with Matchers {



	it should "render single point" in {
		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.write(Fixed2DPoint(0, 0), '-')

		fixedWidthCanvas.print() shouldBe "-"
	}

	it should "render single point along x" in {
		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.write(Fixed2DPoint(2, 0), '-')

		fixedWidthCanvas.print() shouldBe "  -"
	}


	//
	//	it should "render autosignal" in {
	//		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
	//
	//		val box = AutoSignalBox(new AutoSignalComponent("something", 1, 0, matrixUserActor))
	//
	//		box.out shouldBe
	//			"""____
	//				|		 |
	//				|    | something
	//				|<---'""".stripMargin
	//
	//		box.minWidth() shouldBe 15
	//		box.minHeight() shouldBe 4
	//
	//	}
	//
	//	it should "render bisignal" in {
	//		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
	//
	//		val box = AutoSignalBox(new AutoSignalComponent("something", 1, 0, matrixUserActor))
	//
	//		box.out shouldBe
	//			"""____
	//				|		 |
	//				|    | something
	//				|<---'""".stripMargin
	//
	//		box.minWidth() shouldBe 15
	//		box.minHeight() shouldBe 4
	//	}
	//
	//	it should "render entire autosignal" ignore {
	//		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
	//		val matrix = new ViewModelComponents(
	//			mutable.HashMap("user" -> matrixUserActor),
	//			mutable.Buffer(
	//				AutoSignalComponent("something", 0, 0, matrixUserActor)
	//			)
	//		)
	//
	//		val fixedWidthCanvas = new FixedWidthCanvas()
	//
	//		fixedWidthCanvas.out shouldBe
	//			"""    .------.
	//				|  	 | user |
	//				|  	 '------'
	//				|  			 |
	//				|       _|_
	//				|       | |____
	//				|       | |		 |
	//				|       | |    | something
	//				|       | |<---'
	//				|       |_|
	//				|        |
	//				|"""
	//	}
	//
	//	it should "render multiple autosignal" ignore {
	//		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
	//		val matrix = new ViewModelComponents(
	//			mutable.HashMap("user" -> matrixUserActor),
	//			mutable.Buffer(
	//				AutoSignalComponent("something", 0, 0, matrixUserActor),
	//				AutoSignalComponent("something else", 1, 0, matrixUserActor)
	//			)
	//		)
	//
	//		val fixedWidthCanvas = new FixedWidthCanvas()
	//
	//		fixedWidthCanvas.out shouldBe
	//			"""    .------.
	//				|  	 | user |
	//				|  	 '------'
	//				|  			 |
	//				|       _|_
	//				|       | |____
	//				|       | |		 |
	//				|       | |    | something
	//				|       | |<---'
	//				|       | |
	//				|  			| |____
	//				|       | |		 |
	//				|       | |    | something else
	//				|       | |<---'
	//				|       | |
	//				|       |_|
	//				|        |
	//				|"""
	//	}


}
