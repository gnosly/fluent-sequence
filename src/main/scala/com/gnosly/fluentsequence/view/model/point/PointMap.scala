package com.gnosly.fluentsequence.view.model.point

import scala.collection.mutable

class PointMap {
  val defaultOrdering = new Ordering[String] {
    override def compare(a: String, b: String): Int = a.compareTo(b)
  }
  val map: mutable.TreeMap[String, Fixed2dPoint] = mutable.TreeMap[String, Fixed2dPoint]()(defaultOrdering)

  def putAll(entries: Seq[(String, Fixed2dPoint)]): Unit = map ++= entries

  def toMap: ResolvedPoints = new ResolvedPoints(map.toMap)
}
