package com.gnosly.fluentsequence.view.model.point

case class Reference1DPoint(name: String) extends Point1d {
  override def resolve(pointMap: PointMap): Fixed1DPoint = pointMap.get1DPoint(name)

  override def +(i: Point1d): Point1d = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x + b.x))

  override def -(i: Point1d): Point1d = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x - b.x))
}
