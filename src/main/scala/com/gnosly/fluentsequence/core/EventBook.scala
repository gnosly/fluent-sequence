package com.gnosly.fluentsequence.core

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object EventBook {
  def apply(events: Event*): EventBook = {
    val eventBook = new EventBook()
    events.foreach(eventBook.track)
    eventBook
  }
}

case class EventBook(private val events: mutable.Buffer[EventBookEvent] = ArrayBuffer()) {

  def track(event: Event): EventBook = {
    events += EventBookEvent(event)
    this
  }

  def track(books: Seq[EventBook]): EventBook = {
    books.foreach(book => {
      events ++= book.events
    })
    this
  }

  def toEventList: List[EventBookEvent] = events.toList

  def toTimelineEventList: List[TimelineEvent] = events.zipWithIndex.map(a => TimelineEvent(a._2, a._1.event)).toList
}

case class EventBookEvent(event: Event)

case class TimelineEvent(index: Int, event: Event)
