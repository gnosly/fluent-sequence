package com.gnosly.fluentsequence.view.model.component

case class BiSignalComponent(name: String,
                             index: Int,
                             fromActorId: Int,
                             fromActivityId: Int,
                             toActorId: Int,
                             toActivityId: Int)
    extends SignalComponent {

  def leftToRight: Boolean = fromActorId < toActorId
  override def currentIndex: Int = index
}

class SyncRequest(name: String,
                  override val index: Int,
                  override val fromActorId: Int,
                  override val fromActivityId: Int,
                  override val toActorId: Int,
                  override val toActivityId: Int)
    extends BiSignalComponent(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

class SyncResponse(name: String,
                   override val index: Int,
                   override val fromActorId: Int,
                   override val fromActivityId: Int,
                   override val toActorId: Int,
                   override val toActivityId: Int)
    extends BiSignalComponent(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

class AsyncRequest(name: String,
                   override val index: Int,
                   override val fromActorId: Int,
                   override val fromActivityId: Int,
                   override val toActorId: Int,
                   override val toActivityId: Int)
    extends BiSignalComponent(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)
