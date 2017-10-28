package com.gnosly.fluentsequence.core

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object EventBook {
	def apply(events: String*): EventBook = new EventBook(events.map(EventBookEvent(_)).toBuffer)
}

case class EventBook(events: mutable.Buffer[EventBookEvent] = ArrayBuffer()) {

	def track(event: String) = {
		events += EventBookEvent(event)
	}

	def track(books: Seq[EventBook]) = {
		books.foreach(book => {
			events ++= book.events
		})
	}

	def toList: List[TimelineEvent] = events.zipWithIndex.map(a => TimelineEvent(a._2, a._1.eventName)).toList
}


//"ACTOR service REPLIED action TO anotherService"
//"ACTOR service CALLED action TO anotherActor"
//"ACTOR service DOES something"
//"ACTOR service STARTED_NEW_SEQUENCE childSequence"
//"SEQUENCE sequenceName STARTED"

trait Event

case class REPLIED(who: String, something: String, toSomebody: String) extends Event

case class CALLED(who: String, something: String, toSomebody: String) extends Event

case class DONE(who: String, something: String) extends Event

case class NEW_SEQUENCE_SCHEDULED(who: String, sequence: String) extends Event

case class SEQUENCE_STARTED(who: String, sequence: String) extends Event

case class EventBookEvent(eventName: String)

case class TimelineEvent(index: Int, eventName: String)
