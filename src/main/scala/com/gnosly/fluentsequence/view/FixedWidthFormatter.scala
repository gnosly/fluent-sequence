package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.fixedwidth.Fixed2DPoint
import com.gnosly.fluentsequence.view.model.ViewModelComponents

class FixedWidthFormatter(viewModel: ViewModelComponents, painter: FixedWidthPainter) {
	def format(): Map[String, Fixed2DPoint] = Map()

}
