package com.gnosly.fluentsequence.view.fixedwidth

import java.io.Writer

trait Console {
	def printTo(writer: Writer)
}
