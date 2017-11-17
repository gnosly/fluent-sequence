package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas
import com.gnosly.fluentsequence.view.model.{Matrix, MatrixActor}

class FixedWidthCanvas extends Canvas {
	var out: String = ""

	def build(matrix: Matrix) = {
		val actorBoxes = matrix._actors.values.map(a => ActorBox(a).out)
		out += (actorBoxes.toList.apply(0))
	}
}



case class ActorBox(actor: MatrixActor) extends Boxable{
	val padding = 2
	val name = actor.name
	val innerSize = name.length + padding
	val outerSize = innerSize+2

	val out = actorToBox(actor)

	def actorToBox(actor: MatrixActor): String = {
		import Util._

		return "." + r("-", innerSize) + ".\n" +
			"| " + name + " |\n" +
			"'" + r("-", innerSize) + "'\n" +
			r(" ", innerSize / 2) + "|" + r(" ", innerSize / 2) + "\n" //fixme
	}

	override def width(): Unit = outerSize

	override def height(): Unit = 4
}

object Util{
	def r(pattern: String, count: Int): String = (0 until count).map(_ => pattern).reduce(_ + _)
}

trait Boxable {
	def width()
	def height()
}


