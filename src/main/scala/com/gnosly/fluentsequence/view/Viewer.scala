package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.model.{ViewModelComponents, ViewModelComponentsGenerator}

class Viewer(painter: Painter, canvas: Canvas) {
	def view(eventBook: EventBook): String = {
		val viewModel: ViewModelComponents = ViewModelComponentsGenerator.generate(eventBook)
		draw(painter, viewModel)
		canvas.print()
	}

	private def draw(painter: Painter, viewModelComponents: ViewModelComponents) = {
		val pointMap = autoFormatting(viewModelComponents, painter)
		viewModelComponents._actors.foreach(
			a => painter.paint(a._2, pointMap, canvas)
		)
	}

	private def autoFormatting(viewModel: ViewModelComponents,
														 painter: Painter): Map[String, Long] = Map()
}
