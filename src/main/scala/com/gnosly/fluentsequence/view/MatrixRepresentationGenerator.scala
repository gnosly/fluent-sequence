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
		val timelines = actors.map(a => a.liveness).toList

		MatrixRepresentation(actors.toList, activities, signals.toList, timelines)
	}

	private def done(actors: mutable.Buffer[ActorView], signals: mutable.Buffer[Signal],
									 t: TimelineEvent, who: Actor, something: String) = {
		val actor = ActorView(who.name, Step(actors.size))
		actor.startOrContinue(t.index)
		signals += Signal(something,
			PointStep(actor.x, Step(t.index)),
			PointStep(actor.x, Step(t.index + 1))
		)
		actors += actor
	}
}


//
//object ActivityGenerator{
//	def generate(eventBook: EventBook, actorViews:List[ActorView]) = {
//		eventBook.toList
//			.flatMap(event => activityFrom(event))
//	}
//
//	private def activityFrom(t: TimelineEvent) = {
//		t.event match {
//			case DONE(who, _) => List(Activity())
//			case REPLIED(who, _, toSomebody) => List(ActorView(who.name), ActorView(toSomebody.name))
//			case CALLED(who, _, toSomebody) => List(ActorView(who.name), ActorView(toSomebody.name))
//			case NEW_SEQUENCE_SCHEDULED(who, _) => List(ActorView(who.name))
//			case _ => List()
//		}
//	}
//}