package com.gnosly.fluentsequence.view.model.point

case class Variable1DPoint(a: Point1d, b: Point1d, op: (Fixed1DPoint, Fixed1DPoint) => Fixed1DPoint) extends Point1d {
  override def resolve(pointMap: PointMap): Fixed1DPoint = op(a.resolve(pointMap), b.resolve(pointMap))
}
