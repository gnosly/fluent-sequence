package com.gnosly.fluentsequence.core

import org.scalatest.{FlatSpec, Matchers}

class EventBookTest extends FlatSpec with Matchers {

  case class TEST_EVENT(name: String) extends Event

  "EventBook" should "be empty" in {
    val book = EventBook()
    book.toTimelineEventList shouldBe List()
  }

  it should "track new event" in {
    val book = EventBook()
      .track(TEST_EVENT("event1"))
      .track(TEST_EVENT("event2"))
    book.toTimelineEventList shouldBe List(TimelineEvent(0, TEST_EVENT("event1")),
                                           TimelineEvent(1, TEST_EVENT("event2")))
  }

  it should "track a new list of events" in {
    val book1 = EventBook()
      .track(TEST_EVENT("eventA"))
    val book2 = EventBook()
      .track(TEST_EVENT("eventB"))
    val book3 = EventBook()
      .track(TEST_EVENT("eventC"))

    book1.track(book2 :: book3 :: Nil)
    book1.toTimelineEventList shouldBe List(TimelineEvent(0, TEST_EVENT("eventA")),
                                            TimelineEvent(1, TEST_EVENT("eventB")),
                                            TimelineEvent(2, TEST_EVENT("eventC")))
  }

}
