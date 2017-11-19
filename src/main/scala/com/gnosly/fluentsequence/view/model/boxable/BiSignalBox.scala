package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.fixedwidth.Util
import com.gnosly.fluentsequence.view.model.BiSignal

case class BiSignalBox(biSignal: BiSignal) extends Boxable {
	val out = toBox()

	override def toBox() = {
		s"""| ${Util.middle(biSignal.name, 3)}
				|-${Util.r("-", biSignal.name.length)}->""".stripMargin
	}

	override def minWidth() = biSignal.name.length + 3

	override def minHeight() = 2

}
