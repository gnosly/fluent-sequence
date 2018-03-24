package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.model.ActorComponent

trait Canvas {
	def print(): String
	def write(actorComponent: ActorComponent, map: Map[String, Long])
}
