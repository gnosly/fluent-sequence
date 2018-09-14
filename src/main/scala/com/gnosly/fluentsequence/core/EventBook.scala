package com.gnosly.fluentsequence.core

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object EventBook {
  def apply(events: Event*): EventBook = {
    val eventBook = EventBook()
    events.foreach(eventBook.track)
    eventBook
  }
}

case class EventBook() {
  private val events: mutable.Buffer[EventBookEvent] = ArrayBuffer()

  def track(event: Event) = {
    events += EventBookEvent(event)
  }

  def track(books: Seq[EventBook]) = {
    books.foreach(book => {
      events ++= book.events
    })
  }

  def toEventList: List[EventBookEvent] = events.toList

  def toTimelineEventList: List[TimelineEvent] = events.zipWithIndex.map(a => TimelineEvent(a._2, a._1.event)).toList
}

case class EventBookEvent(event: Event)

case class TimelineEvent(index: Int, event: Event)
