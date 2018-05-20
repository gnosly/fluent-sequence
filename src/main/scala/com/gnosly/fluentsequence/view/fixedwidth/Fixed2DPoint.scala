package com.gnosly.fluentsequence.view.fixedwidth

trait Point {
}

trait FixedPoint extends Point {}

trait Point1d extends Point {
	def +(i: Point1d): Point1d

	def -(i: Point1d): Point1d

	def resolve(pointMap: PointMap): Fixed1DPoint
}

trait Point2d extends Point {
	def x(): Point1d

	def y(): Point1d

	def resolve(pointMap: PointMap): VeryFixed2dPoint

	def atY(newY: Long): Point2d

	def atY(newY: Point1d): Point2d

	def left(i: Long): Point2d

	def left(i: Point1d): Point2d

	def right(i: Long): Point2d

	def right(i: Point1d): Point2d

	def up(i: Long): Point2d

	def up(i: Point1d): Point2d

	def down(i: Long): Point2d

	def down(i: Point1d): Point2d
}

case class VeryFixed2dPoint(val x:Long, val y:Long) {
	def right(i: Long): VeryFixed2dPoint = new VeryFixed2dPoint(x + i, y)
	def left(i: Long): VeryFixed2dPoint = new VeryFixed2dPoint(x - i, y)
	def down(i: Long): VeryFixed2dPoint = new VeryFixed2dPoint(x, y + i)
	def up(i: Long) = new VeryFixed2dPoint(x, y - i)
}

case class Fixed2DPoint(_x: Point1d, _y: Point1d) extends Point2d with FixedPoint {

	def this(x: Long, y: Long) = {
		this(Fixed1DPoint(x), Fixed1DPoint(y))
	}

	def resolve(pointMap: PointMap): VeryFixed2dPoint = new VeryFixed2dPoint(x.resolve(pointMap).x, y.resolve(pointMap).x)

	override def x(): Point1d = _x

	override def y(): Point1d = _y

	def atY(newY: Long): Point2d = atY(Fixed1DPoint(newY))

	def atY(newY: Point1d): Point2d = Fixed2DPoint(x, newY)

	def left(i: Long): Point2d = left(Fixed1DPoint(i))

	def left(i: Point1d): Point2d = Fixed2DPoint(x - i, _y)

	def right(i: Long): Point2d = right(Fixed1DPoint(i))

	def right(i: Point1d): Point2d = Fixed2DPoint(x + i, y)

	def up(i: Long): Point2d = up(Fixed1DPoint(i))

	def up(i: Point1d): Point2d = Fixed2DPoint(x, y - i)

	def down(i: Long): Point2d = down(Fixed1DPoint(i))

	def down(i: Point1d): Point2d = Fixed2DPoint(x, y + i)
}

class ReferencePoint(referenceName: String)
	extends Fixed2DPoint(Reference1DPoint(s"$referenceName#x"), Reference1DPoint(s"$referenceName#y")) {
}

case class Fixed1DPoint(x: Long) extends Point1d with FixedPoint {
	override def resolve(pointMap: PointMap): Fixed1DPoint = this

	override def +(i: Point1d) = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x + b.x))

	override def -(i: Point1d) = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x - b.x))

	def <(other: Fixed1DPoint): Boolean = this.x < other.x

	def <=(other: Fixed1DPoint): Boolean = this.x <= other.x
}

case class Reference1DPoint(name: String) extends Point1d {
	override def resolve(pointMap: PointMap): Fixed1DPoint = pointMap.get1DPoint(name)

	override def +(i: Point1d): Point1d = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x + b.x))

	override def -(i: Point1d): Point1d = Variable1DPoint(this, i, (a, b) => Fixed1DPoint(a.x - b.x))
}

case class Variable1DPoint(a: Point1d, b: Point1d, op: (Fixed1DPoint, Fixed1DPoint) => Fixed1DPoint) extends Point1d {
	override def resolve(pointMap: PointMap): Fixed1DPoint = op(a.resolve(pointMap), b.resolve(pointMap))

	override def +(i: Point1d): Point1d = Variable1DPoint(this, i, (a2, b2) => Fixed1DPoint(a2.x + b2.x))

	override def -(i: Point1d): Point1d = Variable1DPoint(this, i, (a2, b2) => Fixed1DPoint(a2.x - b2.x))
}

object PointMath {
	def max(a: Point1d, b: Point1d): Point = {
		Variable1DPoint(a, b, (x, y) => Fixed1DPoint(Math.max(x.x, y.x)))
	}
}