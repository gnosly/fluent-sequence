package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2DPoint, FixedWidthCanvas, Util}
import com.gnosly.fluentsequence.view.model.ActorComponent

class FixedWidthPainter {
	def paint(actor: ActorComponent,
						pointMap: Map[String, Fixed2DPoint],
						canvas: FixedWidthCanvas): Unit = {
		import Util._

		val padding = 2
		val name = actor.name
		val innerSize = name.length + padding

		val topLeftCornerId = pointMap(actor.topLeftCornerId())

		val str = r("-", innerSize)

		canvas.write(topLeftCornerId, "." + str + ".\n")
		canvas.write(topLeftCornerId.down(1), "| " + name + " |\n")
		canvas.write(topLeftCornerId.down(2), "'" + str + "'\n")
		canvas.write(topLeftCornerId.down(3), r(" ", innerSize / 2) + "|" + r(" ", innerSize / 2) + "\n")
	}

}
