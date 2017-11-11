package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.{Activity, ActorView, MatrixRepresentation}

object MatrixRepresentationGenerator extends EventBookGenerator[MatrixRepresentation] {

	def generate(eventBook: EventBook) = {
		MatrixRepresentation(List(), List(), List(), List())
	}
}

object ActorViewGenerator extends EventBookGenerator[List[ActorView]] {
	override def generate(eventBook: EventBook) = {
		eventBook.toList
			.flatMap(event => actorsFrom(event))
			.distinct
	}

	private def actorsFrom(t: TimelineEvent) = {
		t.event match {
			case DONE(who, _) => List(ActorView(who.name))
			case REPLIED(who, _, toSomebody) => List(ActorView(who.name), ActorView(toSomebody.name))
			case CALLED(who, _, toSomebody) => List(ActorView(who.name), ActorView(toSomebody.name))
			case NEW_SEQUENCE_SCHEDULED(who, _) => List(ActorView(who.name))
			case _ => List()
		}
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