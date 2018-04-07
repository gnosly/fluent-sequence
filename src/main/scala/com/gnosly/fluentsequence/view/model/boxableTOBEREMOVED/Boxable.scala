package com.gnosly.fluentsequence.view.model.boxableTOBEREMOVED

trait Boxable {
	def toBox(): String

	def minWidth(): Int

	def minHeight(): Int

	//def render(where,x,y,width,height)
}

trait MatrixPositionable {
	def column(): Int
	def row(): Int
}


/*
row
1 20
2 32
3 12

column
1 21
2 42
3 21
4

qlk deve convertire da column a y e row a x con la MatrixSizes
* */