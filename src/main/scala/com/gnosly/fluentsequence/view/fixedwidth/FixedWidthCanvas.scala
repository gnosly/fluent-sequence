package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas
import com.gnosly.fluentsequence.view.model.{AutoSignal, Matrix, MatrixActor}

class FixedWidthCanvas extends Canvas {
	var out: String = ""

	def build(matrix: Matrix) = {
		val actorBoxes = matrix._actors.values.map(a => ActorBox(a).out)
		out += (actorBoxes.toList.apply(0))
	}
}

//larghezza (*)
//nome actor
//autoSignal
//BiSignal
//

//altezza[*]
//AutoSignal
//Bisignal * numero

//ogni intero index (colonna) => dimensione larghezza => max(*)
//ogni intero index (riga) => dimensione altezza => max[*]
//quindi ho una matrice delle dimensioni
//Creo i pezzi
//li metto in una matrice di celle pixellose (ogni componente sa adattarsi per renderizzarsi secondo le dimensioni)
//Stampo la matrice riga per riga

case class AutoSignalBox(autoSignal: AutoSignal) extends Boxable {
	val out = toBox()

	override def toBox() = {
		s"""____
			 |		 |
			 |    | ${autoSignal.name}
			 |<---'""".stripMargin
	}

	override def width() = autoSignal.name.length + 6

	override def height() = 4

}

case class ActorBox(actor: MatrixActor) extends Boxable {
	val padding = 2
	val name = actor.name
	val innerSize = name.length + padding
	val outerSize = innerSize + 2

	val out = toBox()

	def toBox: String = {
		import Util._

		return "." + r("-", innerSize) + ".\n" +
			"| " + name + " |\n" +
			"'" + r("-", innerSize) + "'\n" +
			r(" ", innerSize / 2) + "|" + r(" ", innerSize / 2) + "\n" //fixme
	}

	override def width() = outerSize

	override def height() = 4
}

object Util {
	def r(pattern: String, count: Int): String = (0 until count).map(_ => pattern).reduce(_ + _)
}

trait Boxable {
	def toBox(): String

	def width(): Int

	def height(): Int
}





