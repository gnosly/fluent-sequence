package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

trait ComponentPainter[T] {

  def paint(model: T, pointMap: Map[String, Fixed2dPoint]): Canvas
}
