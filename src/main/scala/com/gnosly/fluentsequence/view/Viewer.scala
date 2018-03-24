package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.model.{ViewModelComponents, ViewModelComponentsGenerator}

class Viewer(painter: Painter, canvas: Canvas) {
	def view(eventBook: EventBook): String = {
		val viewModel: ViewModelComponents = ViewModelComponentsGenerator.generate(eventBook)
		canvas.print()
	}

	def draw(painter: Painter, viewModelComponents: ViewModelComponents) = {
		val pointMap = autoFormatting(viewModelComponents, painter)
		viewModelComponents._actors.foreach(
			a => canvas.write(a._2, pointMap)
		)
	}

	def autoFormatting(viewModelComponents: ViewModelComponents, painter: Painter): Map[String, Long] = ???
}
