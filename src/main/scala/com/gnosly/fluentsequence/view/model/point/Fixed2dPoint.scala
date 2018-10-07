package com.gnosly.fluentsequence.view.model.point

case class Fixed2dPoint(x: Long, y: Long) {
  def right(i: Long): Fixed2dPoint = Fixed2dPoint(x + i, y)
  def left(i: Long): Fixed2dPoint = Fixed2dPoint(x - i, y)
  def down(i: Long): Fixed2dPoint = Fixed2dPoint(x, y + i)
  def up(i: Long) = Fixed2dPoint(x, y - i)
  def atY(i: Long) = Fixed2dPoint(x, i)
}
