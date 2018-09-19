package com.gnosly.fluentsequence.core

class EventBookReader {

  def read(text: String): EventBook = {
    val eventBook = EventBook()

    text
      .split("\n")
      .map(line => decode(line))
      .foreach(eventBook.track)

    eventBook
  }

  def decode(line: String): Event =
    line.split("\\|") match {
      case Array("SEQUENCE_STARTED", sequence)       => SEQUENCE_STARTED(sequence)
      case Array("DONE", actorType, name, something) => DONE(Actor(typeFrom(actorType), name), something)
      case Array("CALLED", actorType, name, something, toSomebodyType, toSomebodyName) =>
        CALLED(Actor(typeFrom(actorType), name), something, Actor(typeFrom(toSomebodyType), toSomebodyName))
      case Array("FIRED", actorType, name, something, toSomebodyType, toSomebodyName) =>
        FIRED(Actor(typeFrom(actorType), name), something, Actor(typeFrom(toSomebodyType), toSomebodyName))
      case Array("REPLIED", actorType, name, something, toSomebodyType, toSomebodyName) =>
        REPLIED(Actor(typeFrom(actorType), name), something, Actor(typeFrom(toSomebodyType), toSomebodyName))
      case Array("NEW_SEQUENCE_SCHEDULED", actorType, name, sequence) =>
        NEW_SEQUENCE_SCHEDULED(Actor(typeFrom(actorType), name), sequence)
      case Array("SEQUENCE_ENDED", sequence) => SEQUENCE_ENDED(sequence)
    }

  def typeFrom(string: String): ActorType = string match {
    case "USER_TYPE"           => USER_TYPE()
    case "SEQUENCE_ACTOR_TYPE" => SEQUENCE_ACTOR_TYPE()
  }

}
