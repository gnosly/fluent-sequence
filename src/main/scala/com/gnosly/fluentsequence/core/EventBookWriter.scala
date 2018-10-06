package com.gnosly.fluentsequence.core

class EventBookWriter {

  def write(book: EventBook): String = {
    book.toEventList.map(e => encode(e)).reduce(_ + "\n" + _)
  }

  private def encode(e: EventBookEvent): String = e.event match {
    case s: SEQUENCE_STARTED => s"SEQUENCE_STARTED|${s.sequence}"
    case s: DONE             => s"DONE|${s.who.actorType.name}|${s.who.name}|${s.something}"
    case s: CALLED =>
      s"CALLED|${s.who.actorType.name}|${s.who.name}|${s.something}|${s.toSomebody.actorType.name}|${s.toSomebody.name}"
    case s: FIRED =>
      s"FIRED|${s.who.actorType.name}|${s.who.name}|${s.something}|${s.toSomebody.actorType.name}|${s.toSomebody.name}"
    case s: REPLIED =>
      s"REPLIED|${s.who.actorType.name}|${s.who.name}|${s.something}|${s.toSomebody.actorType.name}|${s.toSomebody.name}"
    case s: NEW_SEQUENCE_SCHEDULED => s"NEW_SEQUENCE_SCHEDULED|${s.who.actorType.name}|${s.who.name}|${s.sequence}"
    case s: SEQUENCE_ENDED         => s"SEQUENCE_ENDED|${s.sequence}"
    case s: ALTERNATIVE_STARTED    => s"ALTERNATIVE_STARTED|${s.sequence}"
    case s: ALTERNATIVE_ENDED      => s"ALTERNATIVE_ENDED|${s.sequence}"
  }

}
