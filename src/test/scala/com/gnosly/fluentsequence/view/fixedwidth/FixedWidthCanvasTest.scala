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

	it should "render single point along y" in {
		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.write(Fixed2DPoint(0, 2), '-')

		fixedWidthCanvas.print() shouldBe "\n" +
			"\n" +
			"-"
	}

	it should "render two points " in {
		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.write(Fixed2DPoint(0, 0), '1')
		fixedWidthCanvas.write(Fixed2DPoint(3, 0), '2')

		fixedWidthCanvas.print() shouldBe "1  2"
	}

	it should "render two points in inverse order " in {
		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.write(Fixed2DPoint(3, 0), '2')
		fixedWidthCanvas.write(Fixed2DPoint(0, 0), '1')

		fixedWidthCanvas.print() shouldBe "1  2"
	}

	it should "render multi points " in {
		val fixedWidthCanvas = new FixedWidthCanvas()

		fixedWidthCanvas.write(Fixed2DPoint(0, 0), '1')
		fixedWidthCanvas.write(Fixed2DPoint(3, 0), '2')
		fixedWidthCanvas.write(Fixed2DPoint(0, 3), '3')
		fixedWidthCanvas.write(Fixed2DPoint(3, 3), '4')

		fixedWidthCanvas.print() shouldBe "1  2\n" +
			"\n" +
			"\n" +
			"3  4"
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
