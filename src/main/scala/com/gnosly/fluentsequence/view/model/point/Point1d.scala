package com.gnosly.fluentsequence.view.model.point

import com.gnosly.fluentsequence.view.PointMap

trait Point1d {
	def +(i: Point1d): Point1d

	def -(i: Point1d): Point1d

	def resolve(pointMap: PointMap): Fixed1DPoint
}
