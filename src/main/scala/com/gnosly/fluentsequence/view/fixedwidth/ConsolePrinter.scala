package com.gnosly.fluentsequence.view.fixedwidth

import java.io.OutputStream

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.{MatrixRepresentation, Printer}

object ConsolePrinter extends Printer {
	def print(eventBook: EventBook):Console= {

		val matrix = new MatrixRepresentation(eventBook)
		val fixedWidthCanvas = new FixedWidthCanvas(matrix)
		new Console() {
			override def show(outputStream: OutputStream): Unit = ???
		}
	}

}
