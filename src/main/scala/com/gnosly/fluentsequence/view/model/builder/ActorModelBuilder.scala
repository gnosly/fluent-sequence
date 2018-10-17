package com.gnosly.fluentsequence.view.model.builder

import com.gnosly.fluentsequence.view.model.ViewModels._

import scala.collection.mutable

class ActorModelBuilder(val id: Int,
                        val name: String,
                        val activities: mutable.Buffer[ActivityModelBuilder] = mutable.Buffer[ActivityModelBuilder](),
                        var isLast: Boolean = false) {
  def done(something: String, index: Int): SignalModel = {
    val lastActivity = this.activeUntil(index)
    val autoSignal = AutoSignalModel(something, index, this.id, lastActivity.id)
    lastActivity.right(autoSignal)
    autoSignal
  }

  def replied(called: ActorModelBuilder, something: String, index: Int): SignalModel = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new SyncResponse(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)

    if (id < called.id) {
      lastCallerActivity.right(signal)
      lastCalledActivity.left(signal)
    } else {
      lastCallerActivity.left(signal)
      lastCalledActivity.right(signal)
    }

    signal
  }

  def fired(called: ActorModelBuilder, something: String, index: Int): SignalModel = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new AsyncRequest(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)
    lastCallerActivity.right(signal)
    lastCalledActivity.left(signal)
    signal
  }

  def called(called: ActorModelBuilder, something: String, index: Int): SignalModel = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new SyncRequest(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)
    if (id > called.id) {
      lastCalledActivity.right(signal)
      lastCallerActivity.left(signal)
    } else {
      lastCallerActivity.right(signal)
      lastCalledActivity.left(signal)
    }

    signal
  }

  private def activeUntil(index: Int): ActivityModelBuilder = {
    if (activities.isEmpty) {
      activities += new ActivityModelBuilder(0, this.id, index, index, true)
    }
    val last = activities.last
    if (last.active) {
      last.increaseUntil(index)
      last
    } else {
      val component = new ActivityModelBuilder(last.id + 1, this.id, index, 0, true)
      activities += component
      component
    }
  }

  def build(lastIndex: Int, lastActorId: Int): ActorModel = {
    if (id == lastActorId) {
      markAsLast()
    }
    val activityModels = activities.map(_.build(lastIndex)).toList

    ActorModel(id, name, isLast, activityModels)
  }

  private def markAsLast(): Unit = isLast = true

  override def equals(other: Any): Boolean = other match {
    case that: ActorModelBuilder =>
      (that canEqual this) &&
        id == that.id &&
        name == that.name &&
        activities == that.activities &&
        isLast == that.isLast
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[ActorModelBuilder]

  override def hashCode(): Int = {
    val state = Seq(id, name, activities, isLast)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"ActorComponent($id, $name, $activities, $isLast)"
}
