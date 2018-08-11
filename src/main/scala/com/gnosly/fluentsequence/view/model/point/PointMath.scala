package com.gnosly.fluentsequence.view.model.point

object PointMath {
	def max(a: Point1d, b: Point1d): Point1d = {
		Variable1DPoint(a, b, (x, y) => Fixed1DPoint(Math.max(x.x, y.x)))
	}
}
