package com.gnosly.fluentsequence.view.fixedwidth

import org.scalatest.{FunSuite, Matchers}

class MatrixViewTest extends FunSuite with Matchers{

	test("Navigable by dot"){
		val bottomCell = Cell()
		val rightCell = Cell()
		val topLeft = Cell(bottomCell, rightCell)
		val matrixView = new MatrixView(topLeft)

		matrixView.topLeftCornerCell.right shouldBe rightCell
		matrixView.topLeftCornerCell.bottom shouldBe bottomCell
	}

	test("Get specific point"){
		val bottomCell = Cell()
		val rightCell = Cell()
		val topLeft = Cell(bottomCell, rightCell)
		val matrixView = new MatrixView(topLeft)

		matrixView(0,1) shouldBe rightCell
	}

}
