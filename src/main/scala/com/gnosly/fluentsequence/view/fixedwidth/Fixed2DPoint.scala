package com.gnosly.fluentsequence.view.fixedwidth

import scala.collection.mutable


trait Point {
	def resolve(pointMap: PointMap, oneDimensionMap: SinglePointMap): Fixed2DPoint

	def atY(newY: Long): Point

	def left(i: Long): Point

	def right(i: Long): Point

	def up(i: Long): Point

	def down(i: Long): Point

	def atY(newY: OneDimensionPoint): Point

	def left(i: OneDimensionPoint): Point

	def right(i: OneDimensionPoint): Point

	def up(i: OneDimensionPoint): Point

	def down(i: OneDimensionPoint): Point
}

case class Fixed2DPoint(x: Long, y: Long) extends Point {

	override def resolve(pointMap: PointMap, oneDimensionMap: SinglePointMap): Fixed2DPoint = this

	override def atY(newY: OneDimensionPoint): Point = VariablePoint(this, newY, _.atY(_))
	def atY(newY: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x, newY.x)

	override def atY(newY: Long): Fixed2DPoint = Fixed2DPoint(x, newY)

	override def left(i: OneDimensionPoint): Point = VariablePoint(this, i, _.left(_))
	def left(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x - i.x, y)

	override def left(i: Long): Fixed2DPoint = Fixed2DPoint(x - i, y)

	override def right(i: OneDimensionPoint): Point = VariablePoint(this, i, _.right(_))
	def right(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x + i.x, y)

	override def right(i: Long): Fixed2DPoint = Fixed2DPoint(x + i, y)

	override def up(i: OneDimensionPoint): Point = VariablePoint(this, i, _.up(_))
	def up(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x, y - i.x)

	override def up(i: Long): Fixed2DPoint = Fixed2DPoint(x, y - i)

	override def down(i: OneDimensionPoint): Point = VariablePoint(this, i, _.down(_))
	def down(i: Fixed1DPoint): Fixed2DPoint = Fixed2DPoint(x, y + i.x)

	override def down(i: Long): Fixed2DPoint = Fixed2DPoint(x, y + i)
}

case class ReferencePoint(referenceName: String) extends Point {
	override def resolve(pointMap: PointMap, oneDimensionMap: SinglePointMap) = pointMap(referenceName)

	override def atY(newY: Long): Point = VariablePoint(this, Fixed1DPoint(newY), _.atY(_))

	override def left(i: Long): Point = VariablePoint(this, Fixed1DPoint(i), _.left(_))

	override def right(i: Long): Point = VariablePoint(this, Fixed1DPoint(i), _.right(_))

	override def up(i: Long): Point = VariablePoint(this, Fixed1DPoint(i), _.up(_))

	override def down(i: Long): Point = VariablePoint(this, Fixed1DPoint(i), _.down(_))

	override def atY(newY: OneDimensionPoint): Point = VariablePoint(this, newY, _.atY(_))

	override def left(i: OneDimensionPoint): Point = VariablePoint(this, i, _.left(_))

	override def right(i: OneDimensionPoint): Point = VariablePoint(this, i, _.right(_))

	override def up(i: OneDimensionPoint): Point = VariablePoint(this, i, _.up(_))

	override def down(i: OneDimensionPoint): Point = VariablePoint(this, i, _.down(_))
}

case class VariablePoint(referencePoint: Point, i: OneDimensionPoint, op: (Fixed2DPoint, Fixed1DPoint) => Fixed2DPoint) extends Point {
	override def resolve(pointMap: PointMap, oneDimensionMap: SinglePointMap) =
		op(referencePoint.resolve(pointMap,oneDimensionMap), i.resolve(oneDimensionMap))

	override def atY(newY: Long): Point = carrying(Fixed1DPoint(newY), _.atY(_))

	override def left(i: Long): Point = carrying(Fixed1DPoint(i), _.left(_))

	override def right(i: Long): Point = carrying(Fixed1DPoint(i), _.right(_))

	override def up(i: Long): Point = carrying(Fixed1DPoint(i), _.up(_))

	override def down(i: Long): Point = carrying(Fixed1DPoint(i), _.down(_))

	override def atY(newY: OneDimensionPoint): Point = carrying(newY, _.atY(_))

	override def left(i: OneDimensionPoint): Point = carrying(i, _.left(_))

	override def right(i: OneDimensionPoint): Point = carrying(i, _.right(_))

	override def up(i: OneDimensionPoint): Point = carrying(i, _.up(_))

	override def down(i: OneDimensionPoint): Point = carrying(i, _.down(_))

	private def carrying(i2: OneDimensionPoint, op2: (Fixed2DPoint, Fixed1DPoint) => Fixed2DPoint) = {
		VariablePoint(this, i2, (x, y) => op2(x, y))
	}
}

trait OneDimensionPoint {
	def resolve(pointMap: SinglePointMap): Fixed1DPoint
}

case class Fixed1DPoint(x: Long) extends OneDimensionPoint {
	override def resolve(pointMap: SinglePointMap) = this
}

case class Reference1DPoint(name: String) extends OneDimensionPoint {
	override def resolve(pointMap: SinglePointMap): Fixed1DPoint = pointMap(name)
}

case class Variable1DPoint(a: OneDimensionPoint, b: OneDimensionPoint, op: (Fixed1DPoint,Fixed1DPoint) => Fixed1DPoint) extends OneDimensionPoint {
	override def resolve(pointMap: SinglePointMap): Fixed1DPoint = op(a.resolve(pointMap),b.resolve(pointMap))
}

class SinglePointMap(intervals: mutable.TreeMap[String, Fixed1DPoint] = mutable.TreeMap[String, Fixed1DPoint]()) {
	def updateMax(interval: String, size: Fixed1DPoint): Unit = {
		if (intervals.contains(interval)) {
			if (size.x > intervals(interval).x) {
				intervals.put(interval, size)
			}
		} else {
			intervals.put(interval, size)
		}
	}

	def apply(name: String): Fixed1DPoint = intervals.getOrElse(name, Fixed1DPoint(0))
}

object PointMath {
	def max(a: OneDimensionPoint, b: OneDimensionPoint): OneDimensionPoint ={
		Variable1DPoint(a,b, (x,y) => Fixed1DPoint(Math.max(x.x,y.x)))
	}
}