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

case class EventBookEvent(event: Event)

case class TimelineEvent(index: Int, event: Event)
