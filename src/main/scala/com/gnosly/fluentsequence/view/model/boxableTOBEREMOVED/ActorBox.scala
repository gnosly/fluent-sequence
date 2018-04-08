package com.gnosly.fluentsequence.view.model.boxableTOBEREMOVED

import com.gnosly.fluentsequence.view.fixedwidth.Util
import com.gnosly.fluentsequence.view.model.ActorComponent

case class ActorBox(actor: ActorComponent) extends Boxable with MatrixPositionable{
	val padding = 2
	val name = actor.name
	val innerSize = name.length + padding
	val outerSize = innerSize + 2

	val out = toBox()

	def toBox: String = {
		import Util._

		return "." + r("-", innerSize) + ".\n" +
			"| " + name + " |\n" +
			"'" + r("-", innerSize) + "'\n" +
			r(" ", innerSize / 2) + "|" + r(" ", innerSize / 2) + "\n" //fixme
	}

	override def minWidth() = outerSize

	override def minHeight() = 4

	override def column() = actor.id

	override def row() = 0
}
