package com.gnosly.fluentsequence.view.model.boxable

trait Boxable {
	def toBox(): String

	def minWidth(): Int

	def minHeight(): Int
}

trait MatrixPositionable {
	def column(): Int
	def row(): Int
}
