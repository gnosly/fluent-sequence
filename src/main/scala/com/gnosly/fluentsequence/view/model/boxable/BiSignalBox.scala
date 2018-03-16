package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.fixedwidth.Util
import com.gnosly.fluentsequence.view.model.BiSignal

case class BiSignalBox(biSignal: BiSignal) extends Boxable {
	val out = toBox()

	def render(graphics: Graphics, x:Int, y:Int, width:Int, height:Int) = {
		graphics.drawText(middle(x, width, biSignal.name),y, biSignal.name)
	}

	override def toBox() = {
		s"""| ${Util.middle(biSignal.name, 3)}
				|-${Util.r("-", biSignal.name.length)}->""".stripMargin
	}

	override def minWidth() = biSignal.name.length + 3

	override def minHeight() = 2

	def middle(x: Int, width: Int, text:String): Int = {
		return  x + ( (width - text.length) / 2 )
	}

}
