package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.model.ActorComponent

trait Painter {
	def paint(actorComponent: ActorComponent, pointMap: Map[String, Long], canvas: Canvas): Unit = ???

}
