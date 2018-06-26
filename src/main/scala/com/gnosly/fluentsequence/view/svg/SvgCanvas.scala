package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.{Canvas, Fixed2dPoint}

class SvgCanvas(canvas: String = "") extends Canvas {
  private val multiplier = 10
  private val sb = new StringBuilder(canvas)

  def drawRect(p: Fixed2dPoint, width: Long, height: Long): Unit = {
    sb ++= s"""<rect x="${multiplier * p.x}" y="${multiplier * p.y}" width="${multiplier * width}" height="${multiplier * height}" style="stroke-width: 2.0;stroke: black;fill: none" />"""
  }

  def drawText(p: Fixed2dPoint, text: String) = {
    sb ++= s"""<text x="${multiplier * p.x}" y="${multiplier * p.y}" font-size="16px">${text}</text>"""
  }

  override def print(): String = """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="300" width="200">""" + sb.toString() + """</svg>"""

  def content(): String = sb.toString()

  def merge(other: SvgCanvas): SvgCanvas = new SvgCanvas(this.sb.append(other.sb.toString()).toString())
}
