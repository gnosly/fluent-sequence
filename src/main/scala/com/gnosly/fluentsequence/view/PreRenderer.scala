package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.component.{ActivityComponent, ActorComponent, SignalComponent}

trait PreRenderer {
	def preRender(actor: ActorComponent): Box

	def preRender(activity: ActivityComponent): Box

	def preRender(signal: SignalComponent): Box
}