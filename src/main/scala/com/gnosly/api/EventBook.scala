package com.gnosly.api

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object EventBook {
	def apply(events: String*): EventBook = new EventBook(events.map(InnerEvent(_)).toBuffer)
}

case class EventBook(events: mutable.Buffer[InnerEvent] = ArrayBuffer()) {

	def track(event: String) = {
		events += InnerEvent(event)
	}

	def track(books: Seq[EventBook]) = {
		books.foreach(book => {
			events ++= book.events
		})
	}

	def toList: List[Event] = events.zipWithIndex.map(a => Event(a._2,a._1.eventName)).toList
}

//"ACTOR service REPLIED action TO anotherService"
//"ACTOR service CALLED action TO anotherActor"
//"ACTOR service DOES something"
//"SEQUENCE childSequence STARTED"
//"ACTOR service STARTED_NEW_SEQUENCE childSequence"
//"SEQUENCE sequenceName STARTED"

case class InnerEvent(eventName:String)
case class Event(index: Int, eventName: String)