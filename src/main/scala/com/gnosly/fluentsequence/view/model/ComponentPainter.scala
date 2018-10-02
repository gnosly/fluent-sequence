package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints

trait ComponentPainter[T] {

  def paint(model: T, pointMap: ResolvedPoints): Canvas
}
