package com.gnosly.fluentsequence.view.model.point

trait Point1d {
  import Point1dFunction._

  def +(i: Point1d): Point1d = Variable1DPoint(this, i, sumFunction)

  def -(i: Point1d): Point1d = Variable1DPoint(this, i, subtractFunction)

  def max(that: Point1d): Point1d = Variable1DPoint(this, that, maxFunction)

  def resolve(pointMap: ResolvedPoints): Fixed1DPoint
}

object Point1dFunction {
  val sumFunction = new Function2[Fixed1DPoint, Fixed1DPoint, Fixed1DPoint]() {
    override def toString(): String = " + "
    override def apply(a: Fixed1DPoint, b: Fixed1DPoint): Fixed1DPoint = Fixed1DPoint(a.x + b.x)
  }

  val subtractFunction = new Function2[Fixed1DPoint, Fixed1DPoint, Fixed1DPoint]() {
    override def toString(): String = " - "
    override def apply(a: Fixed1DPoint, b: Fixed1DPoint): Fixed1DPoint = Fixed1DPoint(a.x - b.x)
  }

  val maxFunction = new Function2[Fixed1DPoint, Fixed1DPoint, Fixed1DPoint] {
    override def toString = " max "
    override def apply(v1: Fixed1DPoint, v2: Fixed1DPoint): Fixed1DPoint = v1 max v2
  }
}
