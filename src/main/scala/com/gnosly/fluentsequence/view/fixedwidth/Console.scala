package com.gnosly.fluentsequence.view.fixedwidth

import java.io.OutputStream

trait Console {
	def show(outputStream: OutputStream)
}
