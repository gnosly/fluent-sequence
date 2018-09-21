package com.gnosly.fluentsequence.view.model.point

import scala.collection.mutable

class PointMap {
  val defaultOrdering = new Ordering[String] {
    override def compare(a: String, b: String): Int = a.compareTo(b)
  }
  val map: mutable.TreeMap[String, Fixed2dPoint] = mutable.TreeMap[String, Fixed2dPoint]()(defaultOrdering)

  def get1DPoint(name: String): Fixed1DPoint = {
    if (name.split("#").length > 1) {
      val point = name.split("#")(0)
      val coordinate = name.split("#")(1)

      if (coordinate == "x") {
        Fixed1DPoint(apply(point).x)
      } else {
        Fixed1DPoint(apply(point).y)
      }
    } else {
      Fixed1DPoint(apply(name).x) // x for convention
    }
  }

  private def apply(key: String): Fixed2dPoint =
    map.getOrElse(key, Fixed2dPoint(0, 0))

  def put1DPoint(entries: Seq[(String, Fixed1DPoint)]) {
    putAll(entries.map(x => (x._1, Fixed2dPoint(x._2.x, 0)))) // x for convention
  }

  def putAll(entries: Seq[(String, Fixed2dPoint)]): Unit = map ++= entries

  def toMap: Map[String, Fixed2dPoint] = map.toMap
}
