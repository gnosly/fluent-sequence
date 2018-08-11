package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

trait Painter {
	def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas
}
