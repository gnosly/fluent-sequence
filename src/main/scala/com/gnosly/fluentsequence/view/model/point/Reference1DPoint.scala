package com.gnosly.fluentsequence.view.model.point

case class Reference1DPoint(name: String) extends Point1d {
  override def resolve(pointMap: PointMap): Fixed1DPoint = pointMap.get1DPoint(name)
  override def toString: String = s"'${name}'"
}
