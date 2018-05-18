package com.gnosly.fluentsequence.view.fixedwidth

trait Point {
	def resolve(pointMap: PointMap): FixedPoint
}

trait FixedPoint extends Point {

}

trait Point1d extends Point {
	def resolve(pointMap: PointMap): Fixed1DPoint
}


trait Point2d extends Point {
	override def resolve(pointMap: PointMap): Fixed2DPoint
}

case class Fixed2DPoint(x: Long, y: Long) extends Point2d with FixedPoint {

	def resolve(pointMap: PointMap): Fixed2DPoint = this

	def atY(newY: Point1d): Point2d = VariablePoint(this, newY, _.atY(_))

	def atY(newY: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x, newY.x)

	def atY(newY: Long): Fixed2DPoint = Fixed2DPoint(x, newY)

	def left(i: Point1d): Point2d = VariablePoint(this, i, _.left(_))

	def left(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x - i.x, y)

	def left(i: Long): Fixed2DPoint = Fixed2DPoint(x - i, y)

	def right(i: Point1d): Point2d = VariablePoint(this, i, _.right(_))

	def right(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x + i.x, y)

	def right(i: Long): Fixed2DPoint = Fixed2DPoint(x + i, y)

	def up(i: Point1d): Point2d = VariablePoint(this, i, _.up(_))

	def up(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x, y - i.x)

	def up(i: Long): Fixed2DPoint = Fixed2DPoint(x, y - i)

	def down(i: Point1d): Point2d = VariablePoint(this, i, _.down(_))

	def down(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x, y + i.x)

	def down(i: Long): Fixed2DPoint = Fixed2DPoint(x, y + i)
}

case class ReferencePoint(referenceName: String) extends Point2d {
	def x() = Reference1DPoint(s"$referenceName#x")
	def y() = Reference1DPoint(s"$referenceName#x")

	override def resolve(pointMap: PointMap): Fixed2DPoint = pointMap(referenceName)

	def atY(newY: Long): Point2d = VariablePoint(this, Fixed1DPoint(newY), _.atY(_))

	def left(i: Long): Point2d = VariablePoint(this, Fixed1DPoint(i), _.left(_))

	def right(i: Long): Point2d = VariablePoint(this, Fixed1DPoint(i), _.right(_))

	def up(i: Long): Point2d = VariablePoint(this, Fixed1DPoint(i), _.up(_))

	def down(i: Long): Point2d = VariablePoint(this, Fixed1DPoint(i), _.down(_))

	def atY(newY: Point1d): Point2d = VariablePoint(this, newY, _.atY(_))

	def left(i: Point1d): Point2d = VariablePoint(this, i, _.left(_))

	def right(i: Point1d): Point2d = VariablePoint(this, i, _.right(_))

	def up(i: Point1d): Point2d = VariablePoint(this, i, _.up(_))

	def down(i: Point1d): Point2d = VariablePoint(this, i, _.down(_))
}

case class VariablePoint(referencePoint: Point2d, i: Point1d, op: (Fixed2DPoint, Fixed1DPoint) => Fixed2DPoint) extends Point2d {
	override def resolve(pointMap: PointMap) = op(referencePoint.resolve(pointMap), i.resolve(pointMap))

	def atY(newY: Long): Point2d = carrying(Fixed1DPoint(newY), _.atY(_))

	def left(i: Long): Point2d = carrying(Fixed1DPoint(i), _.left(_))

	def right(i: Long): Point2d = carrying(Fixed1DPoint(i), _.right(_))

	private def carrying(i2: Point1d, op2: (Fixed2DPoint, Fixed1DPoint) => Fixed2DPoint) = {
		VariablePoint(this, i2, (x, y) => op2(x, y))
	}

	def up(i: Long): Point2d = carrying(Fixed1DPoint(i), _.up(_))

	def down(i: Long): Point2d = carrying(Fixed1DPoint(i), _.down(_))

	def atY(newY: Point1d): Point2d = carrying(newY, _.atY(_))

	def left(i: Point1d): Point2d = carrying(i, _.left(_))

	def right(i: Point1d): Point2d = carrying(i, _.right(_))

	def up(i: Point1d): Point2d = carrying(i, _.up(_))

	def down(i: Point1d): Point2d = carrying(i, _.down(_))
}

case class Fixed1DPoint(x: Long) extends Point1d with FixedPoint {
	override def resolve(pointMap: PointMap): Fixed1DPoint = this
}

case class Reference1DPoint(name: String) extends Point1d {
	override def resolve(pointMap: PointMap): Fixed1DPoint = pointMap.get1DPoint(name)
}

case class Variable1DPoint(a: Point1d, b: Point1d, op: (Fixed1DPoint, Fixed1DPoint) => Fixed1DPoint) extends Point1d {
	override def resolve(pointMap: PointMap): Fixed1DPoint = op(a.resolve(pointMap), b.resolve(pointMap))
}


object PointMath {
	def max(a: Point1d, b: Point1d): Point = {
		Variable1DPoint(a, b, (x, y) => Fixed1DPoint(Math.max(x.x, y.x)))
	}
}