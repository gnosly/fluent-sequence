package com.gnosly.fluentsequence.view.fixedwidth

case class Fixed2DPoint(x: Long, y: Long) {

	def left(i: Long): Fixed2DPoint = Fixed2DPoint(x - i, y)

	def right(i: Long): Fixed2DPoint = Fixed2DPoint(x + i, y)

	def up(i: Long): Fixed2DPoint = Fixed2DPoint(x, y - i)

	def down(i: Long): Fixed2DPoint = Fixed2DPoint(x, y + i)
}
