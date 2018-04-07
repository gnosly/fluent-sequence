package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.model.{ViewModelComponents, ViewModelComponentsGenerator}

class FixedWidthViewer {
	private val fixedWidthPainter = new FixedWidthPainter()


	def view(eventBook: EventBook): String = {
		val fixedWidthCanvas = new FixedWidthCanvas()
		val viewModel: ViewModelComponents = ViewModelComponentsGenerator.generate(eventBook)
		draw(fixedWidthPainter, fixedWidthCanvas, viewModel)
		fixedWidthCanvas.print()
	}

	private def draw(painter: FixedWidthPainter,
									 fixedWidthCanvas:FixedWidthCanvas,
									 viewModelComponents: ViewModelComponents): Unit = {
		val pointMap = autoFormatting(viewModelComponents, painter)
		viewModelComponents._actors.foreach(
			a => painter.paint(a._2, pointMap, fixedWidthCanvas)
		)
	}

	private def autoFormatting(viewModel: ViewModelComponents,
														 painter: FixedWidthPainter): Map[String, Fixed2DPoint] = {
		new FixedWidthFormatter(painter).format(viewModel)
	}
}
