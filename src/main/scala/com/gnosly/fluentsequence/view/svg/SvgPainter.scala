package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.ViewModelComponents
import com.gnosly.fluentsequence.view.{Canvas, Fixed2dPoint, Painter}

//fixme maybe could be one painter
case class SvgPainter() extends Painter{
	override def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas = ???
}
