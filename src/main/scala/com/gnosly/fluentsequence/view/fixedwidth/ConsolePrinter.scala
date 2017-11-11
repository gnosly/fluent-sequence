package com.gnosly.fluentsequence.view.fixedwidth

import java.io.{OutputStream, Writer}

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.Printer
import com.gnosly.fluentsequence.view.model.MatrixRepresentation

object ConsolePrinter extends Printer {
	def print(eventBook: EventBook):Console= {

		//val matrix = new MatrixRepresentation()
//		val fixedWidthCanvas = new FixedWidthCanvas(matrix)
		new Console() {
			override def show(writer: Writer): Unit = ???
		}
	}

}
