package com.gnosly.fluentsequence.view.model
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

trait ComponentPainter[T] {

  def paint(model: T, pointMap: ResolvedPoints): Canvas
}
