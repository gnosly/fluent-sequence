package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint

class SvgCanvas(canvas: String = "", width: Long = 0, height: Long = 0) extends Canvas {

  private val MULTIPLIER = 10
  private val STROKE_WIDTH = 2
  private val ARROW_HEIGHT = 5
  private val sb = new StringBuilder(canvas)

  def drawRect(p: Fixed2dPoint, width: Long, height: Long): SvgCanvas = {
    sb ++= s"""<rect x="${MULTIPLIER * p.x}" y="${MULTIPLIER * p.y}" width="${MULTIPLIER * width}" height="${MULTIPLIER * height}" style="stroke-width: 2.0;stroke: black;fill: white" />\n"""
    this
  }

  def drawText(p: Fixed2dPoint, text: String, textAnchor: String = "start"): SvgCanvas = {
    sb ++= s"""<text x="${MULTIPLIER * p.x}" y="${MULTIPLIER * p.y}" font-size="16px" text-anchor="${textAnchor}">${text}</text>\n"""
    this
  }

  def drawLine(from: Fixed2dPoint, to: Fixed2dPoint): SvgCanvas = {
    sb ++= s"""<line x1="${MULTIPLIER * from.x}" y1="${MULTIPLIER * from.y}" x2="${MULTIPLIER * to.x}" y2="${MULTIPLIER * to.y}" style="stroke:black;stroke-width:2;stroke-dasharray:5,5" />\n"""
    this
  }

  def drawRightArrow(from: Fixed2dPoint, to: Fixed2dPoint, lineStyle: String = ""): SvgCanvas = {
    val toX = MULTIPLIER * to.x - STROKE_WIDTH
    val toY = MULTIPLIER * to.y
    sb ++= s"""<line x1="${MULTIPLIER * from.x}" y1="${MULTIPLIER * from.y}" x2="${toX}" y2="${toY}" style="stroke:black;stroke-width:1.5${lineStyle};" />\n"""

    val arrowStartX = toX - 10
    val arrowTopLeftY = toY - ARROW_HEIGHT
    val arrowBottomLeftY = toY + ARROW_HEIGHT
    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${arrowStartX},${arrowTopLeftY} ${toX},${toY} ${arrowStartX},${arrowBottomLeftY}"/>\n"""
    this
  }

  def drawLeftArrow(from: Fixed2dPoint, to: Fixed2dPoint): SvgCanvas = {
    val fromX = MULTIPLIER * from.x + STROKE_WIDTH
    val fromY = MULTIPLIER * from.y
    val toX = MULTIPLIER * to.x
    val toY = MULTIPLIER * to.y
    sb ++= s"""<line x1="${fromX}" y1="${fromY}" x2="${toX}" y2="${toY}" style="stroke:black;stroke-width:1.5;" />\n"""

    val arrowStartX = fromX + 10
    val arrowTopLeftY = fromY - ARROW_HEIGHT
    val arrowBottomLeftY = fromY + ARROW_HEIGHT
    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${arrowStartX},${arrowTopLeftY} ${fromX},${fromY} ${arrowStartX},${arrowBottomLeftY}"/>\n"""
    this
  }

  def drawAutoArrow(from: Fixed2dPoint, to: Fixed2dPoint): SvgCanvas = {
    val fromX = MULTIPLIER * from.x
    val fromY = MULTIPLIER * from.y
    val toX = MULTIPLIER * to.x + STROKE_WIDTH
    val toY = MULTIPLIER * to.y
    val arrowStartX = toX + 10
    val arrowTopLeftY = toY - ARROW_HEIGHT
    val arrowBottomLeftY = toY + ARROW_HEIGHT

    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${fromX},${fromY} ${fromX + 20},${fromY} ${fromX + 20},${toY} ${toX},${toY}"/>\n"""
    sb ++= s"""<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="${arrowStartX},${arrowTopLeftY} ${toX},${toY} ${arrowStartX},${arrowBottomLeftY}"/>\n"""
    this
  }

  def drawBox(p: Fixed2dPoint, width: Long, height: Long, title: String): SvgCanvas = {
    val fromX = MULTIPLIER * p.x
    val fromY = MULTIPLIER * p.y
    val bottomLineY = fromY + 30
    val bottomLineXEnd = fromX + title.length * 8
    val textStartX = fromX + 10
    val textStartY = fromY + 20

    sb ++=
      s"""<rect x="${fromX}" y="${fromY}" width="${MULTIPLIER * width}" height="${MULTIPLIER * height}" style="stroke-width: 2.0;stroke: black;fill: transparent" />
				 |<rect x="${fromX + 1}" y="${fromY + 1}" width="${bottomLineXEnd - fromX - 1}" height="${bottomLineY - fromY - 1}" style="stroke-width: 0;stroke: black;fill: white" />
				 |<text x="${textStartX}" y="${textStartY}" font-size="16px" text-anchor="start">$title</text>
				 |<line x1="${fromX}" y1="${bottomLineY}" x2="${bottomLineXEnd}" y2="${bottomLineY}" style="stroke:black;stroke-width:1.5;" />
				 |<line x1="${bottomLineXEnd}" y1="${bottomLineY}" x2="${bottomLineXEnd + 20}" y2="${fromY}" style="stroke:black;stroke-width:1.5;" />""".stripMargin
    this
  }

  override def print(): String = {
    val backgroundBox = """<rect width="100%" height="100%" fill="white"/>"""
    s"""<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="${height}" width="${width}">$backgroundBox""" + "\n" + sb.toString + """</svg>""" + "\n"
  }

  def content: String = sb.toString

  def merge(other: SvgCanvas): SvgCanvas = new SvgCanvas(this.sb.append(other.sb.toString).toString)

  def withSize(width: Long, height: Long): Canvas =
    new SvgCanvas(this.sb.toString(), MULTIPLIER * width, MULTIPLIER * height)
}
