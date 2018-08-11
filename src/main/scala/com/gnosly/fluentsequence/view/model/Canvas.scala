package com.gnosly.fluentsequence.view.model

trait Canvas {

	def print(): String

	override def toString = print()
}
