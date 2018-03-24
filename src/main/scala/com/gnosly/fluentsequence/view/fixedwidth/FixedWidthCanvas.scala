package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas

import scala.collection.mutable

class FixedWidthCanvas extends Canvas {

	private val canvas = mutable.HashMap[Fixed2DPoint, Character]()

	override def print(): String = ???

	def write(point: Fixed2DPoint, character: Character): Unit = {

	}
}

case class Fixed2DPoint(x: Long, y: Long)












