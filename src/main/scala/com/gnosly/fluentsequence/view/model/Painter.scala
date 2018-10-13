package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.model.ViewModels.ViewModel
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

trait Painter {
  def paint(viewModel: ViewModel, pointMap: ResolvedPoints): Canvas
}
