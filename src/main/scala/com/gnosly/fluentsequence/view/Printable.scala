package com.gnosly.fluentsequence.view

import java.io.Writer

trait Printable {
	def on(writer: Writer)
}
