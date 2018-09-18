package com.gnosly.fluentsequence.core

class EventBookReader {

  def read(text: String): EventBook = {
    val eventBook = new EventBook()

    text
      .split("\n")
      .map(line => decode(line))
      .foreach(eventBook.track(_))

    eventBook
  }

  def decode(line: String): Event = {
    val columns = line.split("\\|")
    val eventType = columns.head
    eventType match {
      case "SEQUENCE_STARTED" => SEQUENCE_STARTED(columns(1))
      case "DONE"             => DONE(Actor(typeFrom(columns(1)), columns(2)), columns(3))
      case "CALLED" =>
        CALLED(Actor(typeFrom(columns(1)), columns(2)), columns(3), Actor(typeFrom(columns(4)), columns(5)))
      case "REPLIED" =>
        REPLIED(Actor(typeFrom(columns(1)), columns(2)), columns(3), Actor(typeFrom(columns(4)), columns(5)))
      case "NEW_SEQUENCE_SCHEDULED" => NEW_SEQUENCE_SCHEDULED(Actor(typeFrom(columns(1)), columns(2)), columns(3))
      case "SEQUENCE_ENDED"         => SEQUENCE_ENDED(columns(1))
    }
  }

  def typeFrom(string: String): ActorType = string match {
    case "USER_TYPE"           => USER_TYPE()
    case "SEQUENCE_ACTOR_TYPE" => SEQUENCE_ACTOR_TYPE()
  }

}
