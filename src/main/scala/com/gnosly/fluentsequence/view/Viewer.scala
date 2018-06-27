package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.formatter.FixedWidthFormatter
import com.gnosly.fluentsequence.view.model.ViewModelComponentsFactory

class Viewer(painter:Painter){
  val formatter = new FixedWidthFormatter(new FixedPreRenderer())

  def view(eventBook: EventBook): Canvas = {
    val viewModel = ViewModelComponentsFactory.createFrom(eventBook)
    val pointMap = formatter.format(viewModel).toMap
    val canvas = painter.paint(viewModel, pointMap)
    canvas
  }
}