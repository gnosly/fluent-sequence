package com.gnosly.fluentsequence.view.fixedwidth

import scala.collection.mutable


trait Point {
	def resolve(pointMap: PointMap): Fixed2DPoint

	def atY(newY: Long): Point

	def left(i: Long): Point

	def right(i: Long): Point

	def up(i: Long): Point

	def down(i: Long): Point
}

case class Fixed2DPoint(x: Long, y: Long) extends Point {

	override def resolve(pointMap: PointMap): Fixed2DPoint = this

	override def atY(newY: Long): Fixed2DPoint = Fixed2DPoint(x, newY)

	override def left(i: Long): Fixed2DPoint = Fixed2DPoint(x - i, y)

	override def right(i: Long): Fixed2DPoint = Fixed2DPoint(x + i, y)

	override def up(i: Long): Fixed2DPoint = Fixed2DPoint(x, y - i)

	override def down(i: Long): Fixed2DPoint = Fixed2DPoint(x, y + i)

}

case class ReferencePoint(referenceName: String) extends Point {
	override def resolve(pointMap: PointMap) = pointMap(referenceName)

	override def atY(newY: Long): Point = VariablePoint(this, newY, _.atY(_))

	override def left(i: Long): Point = VariablePoint(this, i, _.left(_))

	override def right(i: Long): Point = VariablePoint(this, i, _.right(_))

	override def up(i: Long): Point = VariablePoint(this, i, _.up(_))

	override def down(i: Long): Point = VariablePoint(this, i, _.down(_))
}

case class VariablePoint(referencePoint: ReferencePoint, i: Long, op: (Fixed2DPoint, Long) => Point) extends Point {
	override def resolve(pointMap: PointMap) = op(pointMap(referencePoint.referenceName), i).resolve(pointMap)

	override def atY(newY: Long): Point = carrying(newY, _.atY(_))

	private def carrying(i2: Long, op2: (Point, Long) => Point) = {
		VariablePoint(referencePoint, i2, (x, y) => op2(op(x, i), y))
	}

	override def left(i: Long): Point = carrying(i, _.left(_))

	override def right(i: Long): Point = carrying(i, _.right(_))

	override def up(i: Long): Point = carrying(i, _.up(_))

	override def down(i: Long): Point = carrying(i, _.down(_))
}

trait OneDimensionPoint {
	def resolve(pointMap: SinglePointMap): Fixed1DPoint
}
case class Fixed1DPoint(x:Long) extends OneDimensionPoint {
	override def resolve(pointMap: SinglePointMap) = this
}

case class Reference1DPoint(name:String) extends OneDimensionPoint{
	override def resolve(pointMap: SinglePointMap): Fixed1DPoint = pointMap(name)
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