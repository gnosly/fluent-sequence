package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.model.{ViewModelComponents, ViewModelComponentsGenerator}

class FixedWidthViewer {
	private val fixedWidthPainter = new FixedWidthPainter()
	private val fixedWidthCanvas = new FixedWidthCanvas()

	def view(eventBook: EventBook): String = {
		val viewModel: ViewModelComponents = ViewModelComponentsGenerator.generate(eventBook)
		draw(fixedWidthPainter, viewModel)
		fixedWidthCanvas.print()
	}

	private def draw(painter: FixedWidthPainter, viewModelComponents: ViewModelComponents) = {
		val pointMap = autoFormatting(viewModelComponents, painter)
		viewModelComponents._actors.foreach(
			a => painter.paint(a._2, pointMap, fixedWidthCanvas)
		)
	}

	private def autoFormatting(viewModel: ViewModelComponents,
														 painter: FixedWidthPainter): Map[String, Long] = Map()
}
