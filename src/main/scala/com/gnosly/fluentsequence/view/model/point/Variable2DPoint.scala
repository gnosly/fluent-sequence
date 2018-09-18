package com.gnosly.fluentsequence.view.model.point

case class Variable2DPoint(_x: Point1d, _y: Point1d) extends Point2d {

  def this(x: Long, y: Long) = {
    this(Fixed1DPoint(x), Fixed1DPoint(y))
  }

  def resolve(pointMap: PointMap): Fixed2dPoint = new Fixed2dPoint(x.resolve(pointMap).x, y.resolve(pointMap).x)

  def atY(newY: Long): Point2d = atY(Fixed1DPoint(newY))

  def atY(newY: Point1d): Point2d = Variable2DPoint(x, newY)

  def left(i: Long): Point2d = left(Fixed1DPoint(i))

  def left(i: Point1d): Point2d = Variable2DPoint(x - i, _y)

  override def x(): Point1d = _x

  def right(i: Long): Point2d = right(Fixed1DPoint(i))

  def right(i: Point1d): Point2d = Variable2DPoint(x + i, y)

  override def y(): Point1d = _y

  def up(i: Long): Point2d = up(Fixed1DPoint(i))

  def up(i: Point1d): Point2d = Variable2DPoint(x, y - i)

  def down(i: Long): Point2d = down(Fixed1DPoint(i))

  def down(i: Point1d): Point2d = Variable2DPoint(x, y + i)
}
