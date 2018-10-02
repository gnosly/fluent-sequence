package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints

trait Painter {
  def paint(viewModel: ViewModelComponents, pointMap: ResolvedPoints): Canvas
}
