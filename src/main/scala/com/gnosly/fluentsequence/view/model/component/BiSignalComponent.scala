package com.gnosly.fluentsequence.view.model.component

class BiSignalComponent(val name: String,
                        val index: Int,
                        override val fromActorId: Int,
                        override val fromActivityId: Int,
                        val toActorId: Int,
                        val toActivityId: Int,
                        val biSignalComponentType: BiSignalComponentType)
    extends SignalComponent(index, fromActorId, fromActivityId) {

  def leftToRight: Boolean = fromActorId < toActorId

  override def equals(other: Any): Boolean = other match {
    case that: BiSignalComponent =>
      (that canEqual this) &&
        name == that.name &&
        index == that.index &&
        fromActorId == that.fromActorId &&
        toActorId == that.toActorId
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[BiSignalComponent]

  override def hashCode: Int = {
    val state = Seq(name, index, fromActorId, toActorId)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"BiSignalComponent($name, $index, ${fromActorId}, ${toActorId})"
}

class SyncRequest(name: String,
                     override val index: Int,
                     override val fromActorId: Int,
                     override val fromActivityId: Int,
                     override val toActorId: Int,
                     override val toActivityId: Int)
    extends BiSignalComponent(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId, SYNC())

class SyncResponse(name: String,
                  override val index: Int,
                  override val fromActorId: Int,
                  override val fromActivityId: Int,
                  override val toActorId: Int,
                  override val toActivityId: Int)
  extends BiSignalComponent(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId, SYNC())


class AsyncRequest(name: String,
                   override val index: Int,
                   override val fromActorId: Int,
                   override val fromActivityId: Int,
                   override val toActorId: Int,
                   override val toActivityId: Int)
  extends BiSignalComponent(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId, ASYNC())

trait BiSignalComponentType

case class ASYNC() extends BiSignalComponentType
case class SYNC() extends BiSignalComponentType
