package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas

import scala.collection.mutable

class FixedWidthCanvas extends Canvas {
	private val SHELL_ORDER: Ordering[VeryFixed2dPoint] = new Ordering[VeryFixed2dPoint]() {
		override def compare(a: VeryFixed2dPoint, b: VeryFixed2dPoint): Int = {
			if (a.y < b.y) return -1
			else if(a.y == b.y){
				if(a.x <= b.x){
					return -1
				}else{
					return +1
				}
			}else return +1
		}
	}

	private val canvas = mutable.TreeMap[VeryFixed2dPoint, Character]()(SHELL_ORDER)

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

	def write(topLeftCornerId: VeryFixed2dPoint, str: String): Unit = {
		str.zipWithIndex.foreach(char => write(topLeftCornerId.right(char._2),char._1))

	}

	def write(point: VeryFixed2dPoint, character: Character): Unit = {
		canvas.put(point, character)
	}
}














