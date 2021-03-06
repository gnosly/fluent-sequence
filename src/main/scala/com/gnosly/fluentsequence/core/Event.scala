package com.gnosly.fluentsequence.core

trait Event

case class REPLIED(who: Actor, something: String, toSomebody: Actor) extends Event

case class CALLED(who: Actor, something: String, toSomebody: Actor) extends Event

case class DONE(who: Actor, something: String) extends Event

case class SEQUENCE_STARTED(sequence: String) extends Event

case class SEQUENCE_ENDED(sequence: String) extends Event

case class FIRED(who: Actor, something: String, toSomebody: Actor) extends Event

case class ALTERNATIVE_STARTED(sequence: String) extends Event

case class ALTERNATIVE_ENDED(sequence: String) extends Event
