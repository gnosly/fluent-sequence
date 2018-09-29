package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer
import com.gnosly.fluentsequence.view.formatter.ViewModelFormatter
import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.Painter
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory

abstract class Viewer(painter: Painter) {
  val formatter = new ViewModelFormatter(new FixedPreRenderer)

  def view(eventBook: EventBook): Canvas = {
    val viewModel = ViewModelComponentsFactory.createFrom(eventBook)
    val pointMap = formatter.format(viewModel)
    val canvas = painter.paint(viewModel, pointMap)
    canvas
  }
}
