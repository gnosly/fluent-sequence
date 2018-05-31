package com.gnosly.fluentsequence.view.fixedwidth

import scala.collection.mutable

class PointMap {
	private val defaultOrdering = new Ordering[String]() {
		override def compare(a: String, b: String): Int = a.compareTo(b)
	}
	val map: mutable.TreeMap[String, VeryFixed2dPoint] = mutable.TreeMap[String, VeryFixed2dPoint]()(defaultOrdering)

	def get1DPoint(name: String): Fixed1DPoint = {
		if (name.split("#").size > 1) {
			val point = name.split("#")(0)
			val coordinate = name.split("#")(1)
			if (coordinate.equals("x")) {
				return Fixed1DPoint(apply(point).x)
			} else {
				return Fixed1DPoint(apply(point).y)
			}
		} else {
			return Fixed1DPoint(apply(name).x) // x for convention
		}
	}

	private def apply(key: String): VeryFixed2dPoint = {
		if (map.contains(key))
			return map(key)
		println(s"Not found $key in PointMap")
		return new VeryFixed2dPoint(0, 0)
	}

	def put1DPoint(pair: (String, Fixed1DPoint)) {
		putAll(pair._1 -> VeryFixed2dPoint(pair._2.x, 0) :: Nil) // x for convention
	}

	def putAll(entries: Seq[(String, VeryFixed2dPoint)]) = map ++= entries

	def toMap(): mutable.TreeMap[String, VeryFixed2dPoint] = map
}