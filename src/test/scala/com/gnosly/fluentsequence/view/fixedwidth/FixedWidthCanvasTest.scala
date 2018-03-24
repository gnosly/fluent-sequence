package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.boxable.{ActorBox, AutoSignalBox}
import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent, AutoSignalComponent, ViewModelComponents}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class FixedWidthCanvasTest extends FlatSpec with Matchers {

	it should "render user" in {
		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))

		val box = ActorBox(matrixUserActor)
		box.column() shouldBe 0
		box.out shouldBe
			".------.\n" +
				"| user |\n" +
				"'------'\n" +
				"   |   \n"
	}

	it should "render autosignal" in {
		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))

		val box = AutoSignalBox(new AutoSignalComponent("something", 1, 0, matrixUserActor))

		box.out shouldBe
			"""____
				|		 |
				|    | something
				|<---'""".stripMargin

		box.minWidth() shouldBe 15
		box.minHeight() shouldBe 4

	}

	it should "render bisignal" in {
		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))

		val box = AutoSignalBox(new AutoSignalComponent("something", 1, 0, matrixUserActor))

		box.out shouldBe
			"""____
				|		 |
				|    | something
				|<---'""".stripMargin

		box.minWidth() shouldBe 15
		box.minHeight() shouldBe 4
	}

	it should "render entire autosignal" ignore {
		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val matrix = new ViewModelComponents(
			mutable.HashMap("user" -> matrixUserActor),
			mutable.Buffer(
				AutoSignalComponent("something", 0, 0, matrixUserActor)
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
		val matrixUserActor = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val matrix = new ViewModelComponents(
			mutable.HashMap("user" -> matrixUserActor),
			mutable.Buffer(
				AutoSignalComponent("something", 0, 0, matrixUserActor),
				AutoSignalComponent("something else", 1, 0, matrixUserActor)
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
