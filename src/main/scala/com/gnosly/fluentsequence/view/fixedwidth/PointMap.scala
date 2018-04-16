package com.gnosly.fluentsequence.view.fixedwidth

import scala.collection.mutable

class PointMap {


	private val defaultOrdering = new Ordering[String]() {
		override def compare(a: String, b: String): Int = a.compareTo(b)
	}

	val map: mutable.TreeMap[String, Fixed2DPoint] = mutable.TreeMap[String, Fixed2DPoint]()(defaultOrdering)

	def put(str: String, point: Fixed2DPoint): Option[Fixed2DPoint] = map.put(str, point)

	def apply(key: String): Fixed2DPoint = {
		if (map.contains(key))
			return map(key)
		println(s"Not found $key in PointMap")
		return Fixed2DPoint(0, 0)
	}

	def toMap(): mutable.TreeMap[String, Fixed2DPoint] = map
}
