package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.core.{TimelineEvent, EventBook}
import org.scalatest.{FlatSpec, Matchers}

class EventBookTest extends FlatSpec with Matchers{

	"EventBook" should "be empty" in {
		val book = new EventBook
		book.toList shouldBe List()
	}

	it should "track new event" in {
		val book = new EventBook
		book.track("event1")
		book.track("event2")
		book.toList shouldBe List(TimelineEvent(0, "event1"), TimelineEvent(1, "event2"))
	}

	it should "track a new list of events" in {
		val book1 = new EventBook
		val book2 = new EventBook
		val book3 = new EventBook

		book1.track("eventA")
		book2.track("eventB")
		book3.track("eventC")

		book1.track(book2 :: book3 :: Nil)
		book1.toList shouldBe List(TimelineEvent(0, "eventA"), TimelineEvent(1, "eventB"), TimelineEvent(2, "eventC"))
	}

}
