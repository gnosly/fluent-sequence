package com.gnosly.fluentsequence.view.fixedwidth

import java.io.Writer

trait Printable {
	def on(writer: Writer)
}
