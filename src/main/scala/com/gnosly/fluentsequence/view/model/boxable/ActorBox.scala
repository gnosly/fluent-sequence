package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.fixedwidth.Util
import com.gnosly.fluentsequence.view.model.MatrixActor

case class ActorBox(actor: MatrixActor) extends Boxable {
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
}
