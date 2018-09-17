package com.gnosly.fluentsequence.core

case class Actor(actorType: ActorType, name: String)

trait ActorType {
  def name(): String
}

case class SEQUENCE_ACTOR_TYPE() extends ActorType {
  override def name(): String = "SEQUENCE_ACTOR_TYPE"
}

case class USER_TYPE() extends ActorType {
  override def name(): String = "USER_TYPE"
}
