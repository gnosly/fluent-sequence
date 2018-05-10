package com.gnosly.fluentsequence.view.fixedwidth


trait Point {
	def resolve(pointMap: PointMap):Fixed2DPoint

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

case class ReferencePoint(referenceName:String) extends Point {
	override def resolve(pointMap: PointMap) = pointMap(referenceName)

	override def atY(newY: Long): Point = VariablePoint(this, x => x.atY(newY))

	override def left(i: Long): Point = VariablePoint(this, x => x.left(i))

	override def right(i: Long): Point = VariablePoint(this, x => x.right(i))

	override def up(i: Long): Point = VariablePoint(this, x => x.up(i))

	override def down(i: Long): Point = VariablePoint(this, x => x.down(i))
}

case class VariablePoint(referencePoint: ReferencePoint, op: Fixed2DPoint => Point) extends Point {
	override def resolve(pointMap: PointMap) = op(pointMap(referencePoint.referenceName)).resolve(pointMap)

	override def atY(newY: Long): Point = VariablePoint(referencePoint, x => op(x).atY(newY))

	override def left(i: Long): Point = VariablePoint(referencePoint, x => op(x).left(i))

	override def right(i: Long): Point = ???

	override def up(i: Long): Point = ???

	override def down(i: Long): Point = ???
}
