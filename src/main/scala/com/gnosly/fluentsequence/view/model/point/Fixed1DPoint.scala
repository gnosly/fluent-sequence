package com.gnosly.fluentsequence.view.model.point

import com.gnosly.fluentsequence.view.PointMap

case class Fixed1DPoint(x: Long) extends Point1d {
	override def resolve(pointMap: PointMap): Fixed1DPoint = this

	override def +(i: Point1d) = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x + b.x))

	override def -(i: Point1d) = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x - b.x))

	def <(other: Fixed1DPoint): Boolean = this.x < other.x

	def <=(other: Fixed1DPoint): Boolean = this.x <= other.x
}
