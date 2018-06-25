package com.gnosly.fluentsequence.view

trait ComponentPainter[T] {

  def paint(model: T, pointMap: Map[String, Fixed2dPoint]): Canvas
}
