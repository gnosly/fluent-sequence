package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2DPoint, FixedWidthCanvas, Util}
import com.gnosly.fluentsequence.view.model.ActorComponent

class FixedWidthPainter {
	def preRender(actorComponent: ActorComponent) = {
		Fixed2DPoint(s"| ${actorComponent.name} |".length, 0)
	}

	def paint(actor: ActorComponent,
						pointMap: Map[String, Fixed2DPoint],
						canvas: FixedWidthCanvas): Unit = {
		import Util._

		val padding = 2
		val name = actor.name
		val innerSize = name.length + padding

		val topLeftCornerId = pointMap(actor.topLeftCornerId())

		val str = r("-", innerSize)

		canvas.write(topLeftCornerId, "." + str + ".")
		canvas.write(topLeftCornerId.down(1), "| " + name + " |")
		canvas.write(topLeftCornerId.down(2), "'" + str + "'")
		canvas.write(topLeftCornerId.down(3).right(innerSize / 2), "|")
	}
}
