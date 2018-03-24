package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas

import scala.collection.mutable

class FixedWidthCanvas extends Canvas {

	private val SHELL_ORDER: Ordering[Fixed2DPoint] = new Ordering[Fixed2DPoint]() {
		override def compare(a: Fixed2DPoint, b: Fixed2DPoint): Int = {
			if (a.x <= b.x && a.y <= b.y) {
				return -1
			}

			+1
		}
	}

	private val canvas = mutable.TreeMap[Fixed2DPoint, Character]()(SHELL_ORDER)

	override def print(): String = {
		val result = new mutable.StringBuilder()
		var currentX,currentY = 0l

		for ((point, char) <- canvas) {
			while(currentY < point.y){
				result.append("\n")
				currentY = currentY + 1
				currentX = 0
			}
			while(currentX < point.x){
				result.append(" ")
				currentX = currentX + 1
			}

			result.append(char)
			currentX = currentX + 1
		}

		result.result()
	}

	def write(point: Fixed2DPoint, character: Character): Unit = {
		canvas.put(point, character)
	}
}

case class Fixed2DPoint(x: Long, y: Long)












