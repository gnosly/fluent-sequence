package com.gnosly.fluentsequence.view.model.boxableTOBEREMOVED

import com.gnosly.fluentsequence.view.model.AutoSignalComponent

case class AutoSignalBox(autoSignal: AutoSignalComponent) extends Boxable {
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
