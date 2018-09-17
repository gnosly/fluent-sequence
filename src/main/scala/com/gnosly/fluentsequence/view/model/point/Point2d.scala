package com.gnosly.fluentsequence.view.model.point

trait Point2d {
  def x(): Point1d

  def y(): Point1d

  def resolve(pointMap: PointMap): Fixed2dPoint

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
