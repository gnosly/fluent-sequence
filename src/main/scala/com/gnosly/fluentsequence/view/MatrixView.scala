package com.gnosly.fluentsequence.view

import scala.annotation.tailrec

case class Cell( var bottom: Cell = null, var right: Cell = null) {}

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
class MatrixView(var topLeftCornerCell: Cell = null) {

//	def lastRow(cell:Cell):Cell = {
//		if(cell==null || cell.bottom == null)
//			return cell
//		else
//			return lastRow(cell.bottom)
//	}
//
//	def newRow(): Row = {
//
//		val cell = lastRow(topLeftCornerCell)
//		if(cell==null){
//			topLeftCornerCell = new Cell()
//			return new Row(topLeftCornerCell)
//		}
//		new Row(cell)
//	}


	type Direction = Cell => Cell
	val BOTTOM_DIRECTION: Direction = _.bottom
	val RIGHT_DIRECTION: Direction = _.right

	def row(i: Int): CellTraversable = {
		val firstCellInRow = scan(topLeftCornerCell, i, BOTTOM_DIRECTION)
		new CellTraversable(firstCellInRow, RIGHT_DIRECTION)
	}


	def column(i: Int): CellTraversable = {
		val firstCellInColumn = scan(topLeftCornerCell, i, RIGHT_DIRECTION)
		new CellTraversable(firstCellInColumn, BOTTOM_DIRECTION)
	}

	def apply(row: Int, column: Int): Cell = {
		scan(scan(topLeftCornerCell, row, BOTTOM_DIRECTION), column, RIGHT_DIRECTION)
	}

	@tailrec
	private def scan(cell: Cell, iterationLeft: Int, direction: Direction): Cell = {
		if (iterationLeft == 0) {
			return cell
		}

		scan(direction(cell), iterationLeft - 1, direction)
	}

	class CellTraversable(cell: Cell, direction: Direction) extends Iterable[Cell] {
		override def iterator = new CellIterator(cell, direction)

		override def seq = ??? //FIXME

		override def toString(): String = "" //FIXME

		override protected[this] def newBuilder = ??? //Iterable.newBuilder[Cell] //FIXME
	}

	class CellIterator(cell: Cell, direction: Direction) extends Iterator[Cell] {
		var last = cell

		override def hasNext: Boolean = last != null

		override def next(): Cell = {
			val previousLast = last
			last = direction(last)
			previousLast
		}
	}

	class Row(var cellone: Cell) {
		def append(cell: Cell) = {
			if(cellone == null){
				cellone = Cell()
			}
			cellone.right = cell

			cellone = cell
		}

	}

}





