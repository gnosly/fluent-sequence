package com.gnosly.fluentsequence.view.fixedwidth

import scala.collection.mutable

class PointMap {
	def get1DPoint(name: String): Fixed1DPoint =  {
		if(name.split("#").size > 1){
			val point = name.split("#")(0)
			val coordinate = name.split("#")(1)
			if(coordinate.equals("#x")) {
				return Fixed1DPoint(apply(point).x)
			}else{
				return Fixed1DPoint(apply(point).y)
			}
		}else{
			return Fixed1DPoint(apply(name).x)
		}
	}

	private val defaultOrdering = new Ordering[String]() {
		override def compare(a: String, b: String): Int = a.compareTo(b)
	}

	val map: mutable.TreeMap[String, Fixed2DPoint] = mutable.TreeMap[String, Fixed2DPoint]()(defaultOrdering)

	def putAll(entries:Seq[(String,Fixed2DPoint)]) = map ++= entries

	def apply(key: String): Fixed2DPoint = {
		if (map.contains(key))
			return map(key)
		println(s"Not found $key in PointMap")
		return Fixed2DPoint(0, 0)
	}

	def toMap(): mutable.TreeMap[String, Fixed2DPoint] = map
}


class SinglePointMap(intervals: mutable.TreeMap[String, Fixed1DPoint] = mutable.TreeMap[String, Fixed1DPoint]()) {
	def updateMax(interval: String, size: Fixed1DPoint): Unit = {
		if (intervals.contains(interval)) {
			if (size.x > intervals(interval).x) {
				intervals.put(interval, size)
			}
		} else {
			intervals.put(interval, size)
		}
	}

	def apply(name: String): Fixed1DPoint = intervals.getOrElse(name, Fixed1DPoint(0))
}