package com.gnosly.fluentsequence.view.fixedwidth

import java.io.Writer

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.Printer
import com.gnosly.fluentsequence.view.model.ComponentsGenerator

object ConsolePrinter extends Printer {
	def print(eventBook: EventBook):Printable= {
		//ROLE: passare da lista di eventi a oggetto strutturato
		val matrix = ComponentsGenerator.generate(eventBook)
		//ROLE: passare a una matrice di righe e colonne
		val fixedWidthCanvas	 = new FixedWidthCanvas()
		//ROLE: scrivere su output la matrice
		new Printable() {
			override def on(writer: Writer): Unit = ???
		}
	}

}
