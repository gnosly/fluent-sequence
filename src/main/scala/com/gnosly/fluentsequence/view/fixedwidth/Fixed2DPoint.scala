package com.gnosly.fluentsequence.view.fixedwidth

case class Fixed2DPoint(x: Long, y: Long) {
	def right(i: Long): Fixed2DPoint = Fixed2DPoint(x+i,y)

	def down(i: Long): Fixed2DPoint = Fixed2DPoint(x,y+i)
}
