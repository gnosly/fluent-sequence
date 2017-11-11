package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core.EventBook

case class MatrixRepresentation(val actors: List[ActorView],
													 val activities: List[Activity],
													 val signals: List[Signal],
													 val livenesses: List[Liveness]) {

	def this(eventBook: EventBook) = {
		this(List(),List(),List(),List())
	}

}
