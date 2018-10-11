package com.gnosly.fluentsequence.view.model.component

case class BiSignalModel(name: String,
                         index: Int,
                         fromActorId: Int,
                         fromActivityId: Int,
                         toActorId: Int,
                         toActivityId: Int)
    extends SignalModel {

  def leftToRight: Boolean = fromActorId < toActorId
  override def currentIndex: Int = index
  override def fromActorIdd: Int = fromActorId
}

class SyncRequest(name: String,
                  override val index: Int,
                  override val fromActorId: Int,
                  override val fromActivityId: Int,
                  override val toActorId: Int,
                  override val toActivityId: Int)
    extends BiSignalModel(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

class SyncResponse(name: String,
                   override val index: Int,
                   override val fromActorId: Int,
                   override val fromActivityId: Int,
                   override val toActorId: Int,
                   override val toActivityId: Int)
    extends BiSignalModel(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

class AsyncRequest(name: String,
                   override val index: Int,
                   override val fromActorId: Int,
                   override val fromActivityId: Int,
                   override val toActorId: Int,
                   override val toActivityId: Int)
    extends BiSignalModel(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)
