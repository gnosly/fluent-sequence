package com.gnosly.fluentsequence.core

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object EventBook {
	def apply(events: Event*): EventBook = new EventBook(events.map(EventBookEvent(_)).toBuffer)
}

case class EventBook(events: mutable.Buffer[EventBookEvent] = ArrayBuffer()) {

	def track(event: Event) = {
		events += EventBookEvent(event)
	}

	def track(books: Seq[EventBook]) = {
		books.foreach(book => {
			events ++= book.events
		})
	}

	def toList: List[TimelineEvent] = events.zipWithIndex.map(a => TimelineEvent(a._2, a._1.event)).toList
}


trait Event

case class REPLIED(who: String, something: String, toSomebody: String) extends Event

case class CALLED(who: String, something: String, toSomebody: String) extends Event

case class DONE(entity: ActorType, who: String, something: String) extends Event

case class NEW_SEQUENCE_SCHEDULED(who: String, sequence: String) extends Event

case class SEQUENCE_STARTED(sequence: String) extends Event

trait ActorType

case class ACTOR() extends ActorType

case class USER() extends ActorType

case class EventBookEvent(event: Event)

case class TimelineEvent(index: Int, event: Event)
