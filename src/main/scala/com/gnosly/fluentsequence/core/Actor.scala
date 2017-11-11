package com.gnosly.fluentsequence.core

case class Actor(actorType: ActorType, name:String)

trait ActorType

case class SEQUENCE_ACTOR() extends ActorType

case class USER() extends ActorType
