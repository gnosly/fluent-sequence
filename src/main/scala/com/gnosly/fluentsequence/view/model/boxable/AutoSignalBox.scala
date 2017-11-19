package com.gnosly.fluentsequence.view.model.boxable

import com.gnosly.fluentsequence.view.model.AutoSignal

case class AutoSignalBox(autoSignal: AutoSignal) extends Boxable {
	val out = toBox()

	override def toBox() = {
		s"""____
			 |		 |
			 |    | ${autoSignal.name}
			 |<---'""".stripMargin
	}

	override def minWidth() = autoSignal.name.length + 6

	override def minHeight() = 4

}
