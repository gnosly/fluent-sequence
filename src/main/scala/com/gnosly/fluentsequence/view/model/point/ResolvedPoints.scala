package com.gnosly.fluentsequence.view.model.point

case class ResolvedPoints(map: Map[String, Fixed2dPoint]) {
  def apply(key: String): Fixed2dPoint =
    map.getOrElse(key, Fixed2dPoint(0, 0))

  def contains(key: String): Boolean = {
    map.contains(key)
  }
}
