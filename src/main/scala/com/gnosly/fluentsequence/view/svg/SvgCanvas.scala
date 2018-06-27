package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.{Canvas, Fixed2dPoint}

class SvgCanvas(canvas: String = "") extends Canvas {
  private val multiplier = 10

  private val sb = new StringBuilder(canvas)
  def drawRect(p: Fixed2dPoint, width: Long, height: Long): Unit = {
    sb ++= s"""<rect x="${multiplier * p.x}" y="${multiplier * p.y}" width="${multiplier * width}" height="${multiplier * height}" style="stroke-width: 2.0;stroke: black;fill: white" />\n"""
  }

  def drawText(p: Fixed2dPoint, text: String) = {
    sb ++= s"""<text x="${multiplier * p.x}" y="${multiplier * p.y}" font-size="16px">${text}</text>\n"""
  }

  def drawLine(from: Fixed2dPoint, to: Fixed2dPoint) = {
    sb ++= s"""<line x1="${multiplier * from.x}" y1="${multiplier * from.y}" x2="${multiplier * to.x}" y2="${multiplier * to.y}" style="stroke:black;stroke-width:2;stroke-dasharray:5,5" />\n"""
  }

  override def print(): String = """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="1300" width="1200">""" + "\n" + sb.toString() + """</svg>""" + "\n"

  def content(): String = sb.toString()

  def merge(other: SvgCanvas): SvgCanvas = new SvgCanvas(this.sb.append(other.sb.toString()).toString())
}
