package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints

trait Painter {
  def paint(viewModel: ViewModel, pointMap: ResolvedPoints): Canvas
}
