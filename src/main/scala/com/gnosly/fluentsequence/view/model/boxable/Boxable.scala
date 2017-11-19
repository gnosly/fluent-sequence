package com.gnosly.fluentsequence.view.model.boxable

trait Boxable {
	def toBox(): String

	def minWidth(): Int

	def minHeight(): Int
}
