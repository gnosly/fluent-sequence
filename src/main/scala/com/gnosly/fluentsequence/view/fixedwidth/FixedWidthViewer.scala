package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.Canvas
import com.gnosly.fluentsequence.view.fixedwidth.formatter.FixedWidthFormatter
import com.gnosly.fluentsequence.view.fixedwidth.painter.FixedWidthPainter
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory

class FixedWidthViewer {
  private val painter = new FixedWidthPainter()
  private val formatter = new FixedWidthFormatter(new FixedWidthPreRenderer())

  def view(eventBook: EventBook): Canvas = {
    val viewModel = ViewModelComponentsFactory.createFrom(eventBook)
    val pointMap = formatter.format(viewModel).toMap
    val canvas = painter.paint(viewModel, pointMap)
    canvas
  }
}
