package com.gnosly.fluentsequence.view

import org.scalatest.{FunSuite, Matchers}

class MatrixViewTest extends FunSuite with Matchers {

	test("Navigable by dot") {
		val bottomCell = Cell()
		val rightCell = Cell()
		val topLeft = Cell(bottomCell, rightCell)
		val matrixView = new MatrixView(topLeft)

		matrixView.topLeftCornerCell.right shouldBe rightCell
		matrixView.topLeftCornerCell.bottom shouldBe bottomCell
	}

	test("Get specific point") {
		val bottomCell = Cell()
		val rightCell = Cell()
		val topLeft = Cell(bottomCell, rightCell)
		val matrixView = new MatrixView(topLeft)

		matrixView(0, 1) shouldBe rightCell
	}


	test("vertical navigation") {
		val rightBottom: Cell = Cell()
		val bottomCell = Cell()
		val rightCell = Cell(rightBottom, null)
		val topLeft = Cell(bottomCell, rightCell)
		val matrixView = new MatrixView(topLeft)

		matrixView.column(1).toList shouldBe List(rightCell, rightBottom)
	}

	test("horizontal navigation") {
		val rightBottom: Cell = Cell()
		val bottomCell = Cell(null, rightBottom)
		val topLeft = Cell(bottomCell, Cell())
		val matrixView = new MatrixView(topLeft)

		matrixView.row(1).toList shouldBe List(bottomCell, rightBottom)
	}

}
