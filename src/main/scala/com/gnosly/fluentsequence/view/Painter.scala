package com.gnosly.fluentsequence.view

import Fixed2dPoint
import com.gnosly.fluentsequence.view.model.ViewModelComponents

trait Painter {
	def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas
}
