package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.fixedwidth.formatter.FixedWidthFormatter
import com.gnosly.fluentsequence.view.fixedwidth.painter.FixedWidthPainter
import com.gnosly.fluentsequence.view.model.{ViewModelComponents, ViewModelComponentsFactory}

class FixedWidthViewer {
  private val painter = new FixedWidthPainter()
  private val formatter = new FixedWidthFormatter(painter)

  def view(eventBook: EventBook): String = {
    val viewModel = ViewModelComponentsFactory.createFrom(eventBook)
    val pointMap = formatter.format(viewModel).toMap
    val canvas = painter.paint(viewModel, pointMap)
    canvas.print()
  }
}
