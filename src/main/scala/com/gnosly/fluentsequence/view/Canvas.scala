package com.gnosly.fluentsequence.view

trait Canvas {

	def print(): String

	override def toString = print()
}
