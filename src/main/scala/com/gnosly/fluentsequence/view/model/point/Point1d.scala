package com.gnosly.fluentsequence.view.model.point

trait Point1d {
  def +(i: Point1d): Point1d = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x + b.x))

  def -(i: Point1d): Point1d = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x - b.x))

  def resolve(pointMap: PointMap): Fixed1DPoint

  def max(that: Point1d): Point1d = Variable1DPoint(this, that, (x, y) => Fixed1DPoint(x.x max y.x))
}
