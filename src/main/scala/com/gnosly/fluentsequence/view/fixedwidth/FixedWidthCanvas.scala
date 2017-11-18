package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas
import com.gnosly.fluentsequence.view.model.{AutoSignal, Matrix, MatrixActor}

import scala.annotation.tailrec

class FixedWidthCanvas extends Canvas {
	var out: String = ""

	def build(matrix: Matrix) = {
		val actorBoxes = matrix._actors.values.map(a => ActorBox(a).out)
		out += (actorBoxes.toList.apply(0))
	}
}


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

/*
* MatrixView con navigazione a colonna e a riga per trovare la larghezza e la altezza
*
* B //attori
* BBB /azioni..
* B
*
* B deve fornire la larghezza e l'altezza minima
* B deve fornire una funzione di render(width,height)
* B deve avere util per centrare le scritte
*
* Sarebbe forse comoda una struttura a matrice dove ogni cella è un carattere
* */

case class Cell(bottom: Cell = null, right: Cell = null) {

}

class MatrixView(val topLeftCornerCell: Cell) {

	def apply(row: Int, column: Int):Cell = {
		columnScan(rowScan(topLeftCornerCell, row), column)
	}

	@tailrec
	private def rowScan(cell: Cell, row: Int): Cell = {
		if(row==0){
			return cell;
		}

		rowScan(cell.bottom, row-1)
	}

	@tailrec
	private def columnScan(cell: Cell, column: Int): Cell = {
		if(column==0){
			return cell;
		}

		columnScan(cell.right, column-1)
	}
}