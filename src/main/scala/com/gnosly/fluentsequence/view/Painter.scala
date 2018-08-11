package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.model.ViewModelComponents
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

trait Painter {
	def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas
}
