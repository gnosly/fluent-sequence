package com.gnosly.api

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object EventBook {
	def apply(events: String*): EventBook = new EventBook(events.map(InnerEvent(_)).toBuffer)
}

case class EventBook(events: mutable.Buffer[InnerEvent] = ArrayBuffer()) {

	//	def this(events: List[String]){
	//		this()
	//	}

	def track(books: Seq[EventBook]) = {
		books.foreach(book => {
			events ++= book.events
		})
	}

	def toList: List[Event] = events.zipWithIndex.map(a => Event(a._2,a._1.eventName)).toList

	def track(event: String) = {
		events += InnerEvent(event)
	}
}

case class InnerEvent(eventName:String)
case class Event(index: Int, eventName: String)