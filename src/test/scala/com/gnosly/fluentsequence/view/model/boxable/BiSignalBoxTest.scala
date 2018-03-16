package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.model.BiSignal
import org.scalatest.{FunSuite, Matchers}

import scala.collection.mutable

class BiSignalBoxTest extends FunSuite with Matchers {

	test("out") {
		val biSignalBox = BiSignalBox(BiSignal("something", 1, null, null))
		biSignalBox.out shouldBe
			"  something \n" +
				"----------->"
					.stripMargin
	}

	test("sizing") {
		val signalName = "something"
		val biSignalBox = BiSignalBox(BiSignal(signalName, 1, null, null))
		biSignalBox.minHeight() shouldBe 2
		biSignalBox.minWidth() shouldBe signalName.length + 3
	}

	test("rendering") {
		val biSignalBox = BiSignalBox(BiSignal("something", 1, null, null))

		val graphics = new TextGraphics(20, 2)
		biSignalBox.render(graphics, 0, 0, 20, 10)

		graphics.build() shouldBe
				"     something    \n" +
				"------------------> \n"
	}


	test("graphics") {
		new TextGraphics(2, 2).build() shouldBe "\n\n"
	}

	test("graphics draw text") {
		val graphics = new TextGraphics(5, 1)
		graphics.drawText(1, 0, "text")
		graphics.build() shouldBe " text\n"
	}


	class TextGraphics(width: Int, height: Int) extends Graphics {

		private val out: mutable.Buffer[mutable.Buffer[Option[Char]]] = init(width, height)

		def init(width: Int, height: Int): mutable.Buffer[mutable.Buffer[Option[Char]]] = {
			(0 until height).map(_ =>
				(0 until width).map(_ => {
					val none: Option[Char] = None
					none
				}).toBuffer)
				.toBuffer
		}

		override def drawText(x: Int, y: Int, text: String): Unit = {
			text.toCharArray
				.zipWithIndex
				.foreach(xx => xx match {
					case (char, index) => out(y)(x + index) = Some(char)
				})


		}


		def build(): String = {
			var result = ""
			out.map(
				chars => chars.foldLeft("")(
					(acc, b) => b match {
						case None => acc
						case _ => acc + b.get.toString
					})
			).foreach(line =>
				result += line + "\n"
			)
			result
		}
	}

}