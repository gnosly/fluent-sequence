package com.gnosly.fluentsequence.view.model.point

case class Fixed2dPoint(val x:Long, val y:Long) {
	def right(i: Long): Fixed2dPoint = new Fixed2dPoint(x + i, y)
	def left(i: Long): Fixed2dPoint = new Fixed2dPoint(x - i, y)
	def down(i: Long): Fixed2dPoint = new Fixed2dPoint(x, y + i)
	def up(i: Long) = new Fixed2dPoint(x, y - i)
}
