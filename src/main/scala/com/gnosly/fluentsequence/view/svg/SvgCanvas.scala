package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class SvgCanvas(canvas: String = "") extends Canvas {

  private val STROKE_WIDTH = 2
  private val multiplier = 10
  private val sb = new StringBuilder(canvas)

  def drawRect(p: Fixed2dPoint, width: Long, height: Long): Unit = {
    sb ++= s"""<rect x="${multiplier * p.x}" y="${multiplier * p.y}" width="${multiplier * width}" height="${multiplier * height}" style="stroke-width: 2.0;stroke: black;fill: white" />\n"""
  }

  def drawText(p: Fixed2dPoint, text: String, textAnchor: String = "start") = {
    sb ++= s"""<text x="${multiplier * p.x}" y="${multiplier * p.y}" font-size="16px" text-anchor="${textAnchor}">${text}</text>\n"""
  }

  def drawLine(from: Fixed2dPoint, to: Fixed2dPoint) = {
    sb ++= s"""<line x1="${multiplier * from.x}" y1="${multiplier * from.y}" x2="${multiplier * to.x}" y2="${multiplier * to.y}" style="stroke:black;stroke-width:2;stroke-dasharray:5,5" />\n"""
  }

  private val ARROW_HEIGHT = 5

  def drawRightArrow(from: Fixed2dPoint, to: Fixed2dPoint) = {
    val toX = multiplier * to.x - STROKE_WIDTH
    val toY = multiplier * to.y
    sb ++= s"""<line x1="${multiplier * from.x}" y1="${multiplier * from.y}" x2="${toX}" y2="${toY}" style="stroke:black;stroke-width:1.5;" />\n"""

    val arrowStartX = toX - 10
    val arrowTopLeftY = toY - ARROW_HEIGHT
    val arrowBottomLeftY = toY + ARROW_HEIGHT
    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${arrowStartX},${arrowTopLeftY} ${toX},${toY} ${arrowStartX},${arrowBottomLeftY}"/>\n"""
  }

  def drawLeftArrow(from: Fixed2dPoint, to: Fixed2dPoint) = {
    val fromX = multiplier * from.x + STROKE_WIDTH
    val fromY = multiplier * from.y
    val toX = multiplier * to.x
    val toY = multiplier * to.y
    sb ++= s"""<line x1="${fromX}" y1="${fromY}" x2="${toX}" y2="${toY}" style="stroke:black;stroke-width:1.5;" />\n"""

    val arrowStartX = fromX + 10
    val arrowTopLeftY = fromY - ARROW_HEIGHT
    val arrowBottomLeftY = fromY + ARROW_HEIGHT
    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${arrowStartX},${arrowTopLeftY} ${fromX},${fromY} ${arrowStartX},${arrowBottomLeftY}"/>\n"""
  }

  def drawAutoArrow(from: Fixed2dPoint, to: Fixed2dPoint) = {
    val fromX = multiplier * from.x
    val fromY = multiplier * from.y
    val toX = multiplier * to.x + STROKE_WIDTH
    val toY = multiplier * to.y
    val arrowStartX = toX + 10
    val arrowTopLeftY = toY - ARROW_HEIGHT
    val arrowBottomLeftY = toY + ARROW_HEIGHT

    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${fromX},${fromY} ${fromX + 20},${fromY} ${fromX + 20},${toY} ${toX},${toY}"/>\n"""
    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${arrowStartX},${arrowTopLeftY} ${toX},${toY} ${arrowStartX},${arrowBottomLeftY}"/>\n"""
  }

  override def print(): String =
    """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="5300" width="5300">""" + "\n" + sb
      .toString() + """</svg>""" + "\n"

  def content(): String = sb.toString()

  def merge(other: SvgCanvas): SvgCanvas = new SvgCanvas(this.sb.append(other.sb.toString()).toString())

}
