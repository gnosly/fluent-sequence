package com.gnosly.fluentsequence.view.model

case class BiSignalComponent(name: String, index: Int, fromActor: ActorComponent, toActor: ActorComponent) extends SignalComponent
