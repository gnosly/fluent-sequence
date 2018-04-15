package com.gnosly.fluentsequence.core

case class Actor(actorType: ActorType, name:String)

trait ActorType

case class SEQUENCE_ACTOR_TYPE() extends ActorType

case class USER_TYPE() extends ActorType
