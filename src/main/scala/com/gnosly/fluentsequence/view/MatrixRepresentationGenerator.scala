package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model._

import scala.collection.mutable

object MatrixRepresentationGenerator extends EventBookGenerator[MatrixRepresentation] {

	def generate(eventBook: EventBook) = {

		val actors = mutable.Buffer[ActorView]()
		val signals = mutable.Buffer[Signal]()

		eventBook.toList.foreach(t => {
			t.event match {
				case DONE(who, something) => done(actors, signals, t, who, something)
			}
		})

		val activities = actors.map(a => a.activity).toList
		val livenesses = actors.map(a => a.liveness).toList

		MatrixRepresentation(actors.toList, activities, signals.toList, livenesses)
	}

	private def done(actors: mutable.Buffer[ActorView], signals: mutable.Buffer[Signal],
									 t: TimelineEvent, who: Actor, something: String) = {
		val actor = ActorView(who.name, actors.size)
		actor.startOrContinue(t.index)
		signals += Signal(something, PointStep(actor.x, t.index), PointStep(actor.x, t.index + 1))
		actors += actor
	}


}
