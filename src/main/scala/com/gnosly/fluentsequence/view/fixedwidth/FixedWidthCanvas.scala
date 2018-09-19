package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

import scala.collection.mutable

//Fixme Rename, create a type
object dd {
  val SHELL_ORDER: Ordering[Fixed2dPoint] = (a: Fixed2dPoint, b: Fixed2dPoint) =>
    a.y match {
      case b.y => a.x compareTo b.x
      case _   => a.y compareTo b.y
  }
}

class FixedWidthCanvas(
    protected val canvas: mutable.TreeMap[Fixed2dPoint, Character] =
      mutable.TreeMap[Fixed2dPoint, Character]()(dd.SHELL_ORDER))
    extends Canvas {

  def write(topLeftCornerId: Fixed2dPoint, str: String): FixedWidthCanvas = {
    str.zipWithIndex.foreach(char => write(topLeftCornerId.right(char._2), char._1))
    this
  }

  def write(point: Fixed2dPoint, character: Character): FixedWidthCanvas = {
    canvas.put(point, character)
    this
  }

  def merge(other: FixedWidthCanvas): FixedWidthCanvas = {
    val newCanvas = new FixedWidthCanvas
    this.canvas.foreach(a => newCanvas.write(a._1, a._2))
    other.canvas.foreach(a => newCanvas.write(a._1, a._2))
    newCanvas
  }

  override def print(): String = {
    val result = new mutable.StringBuilder
    var currentX, currentY = 0l

    for ((point, char) <- canvas) {
      while (currentY < point.y) {
        result.append("\n")
        currentY = currentY + 1
        currentX = 0
      }
      while (currentX < point.x) {
        result.append(" ")
        currentX = currentX + 1
      }

      result.append(char)
      currentX = currentX + 1
    }

    result.result()
  }

}
