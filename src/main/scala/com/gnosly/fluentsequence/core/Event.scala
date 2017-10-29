package com.gnosly.fluentsequence.core

trait Event

case class REPLIED(who: Actor, something: String, toSomebody: Actor) extends Event

case class CALLED(who: Actor, something: String, toSomebody: Actor) extends Event

case class DONE(who: Actor, something: String) extends Event

case class NEW_SEQUENCE_SCHEDULED(who: Actor, sequence: String) extends Event

case class SEQUENCE_STARTED(sequence: String) extends Event
