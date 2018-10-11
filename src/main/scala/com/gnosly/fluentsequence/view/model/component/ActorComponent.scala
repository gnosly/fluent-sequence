package com.gnosly.fluentsequence.view.model.component

import scala.collection.mutable

class ActorComponent(val id: Int,
                     val name: String,
                     val activities: mutable.Buffer[ActivityComponent] = mutable.Buffer[ActivityComponent](),
                     var isLast: Boolean = false)
    extends Component {
  def markAsLast: Unit = isLast = true

  def done(something: String, index: Int): SignalModel = {
    val lastActivity = this.activeUntil(index)
    val autoSignal = AutoSignalModel(something, index, this.id, lastActivity.id)
    lastActivity.rightLoop(autoSignal)
    autoSignal
  }

  def replied(called: ActorComponent, something: String, index: Int): SignalModel = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new SyncResponse(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)
    if (signal.leftToRight) {
      lastCallerActivity.right(signal)
      lastCalledActivity.left(signal)
    } else {
      lastCallerActivity.left(signal)
      lastCalledActivity.right(signal)
    }
    signal
  }

  def fired(called: ActorComponent, something: String, index: Int): SignalModel = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new AsyncRequest(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)
    if (signal.leftToRight) {
      lastCallerActivity.right(signal)
      lastCalledActivity.left(signal)
    } else {
      lastCallerActivity.left(signal)
      lastCalledActivity.right(signal)
    }
    signal
  }

  def called(called: ActorComponent, something: String, index: Int): SignalModel = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new SyncRequest(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)
    if (signal.leftToRight) {
      lastCallerActivity.right(signal)
      lastCalledActivity.left(signal)
    } else {
      lastCallerActivity.left(signal)
      lastCalledActivity.right(signal)
    }
    signal
  }

  private def activeUntil(index: Int): ActivityComponent = {
    if (activities.isEmpty) {
      activities += new ActivityComponent(0, this.id, index, index, true)
    }
    val last = activities.last
    if (last.active) {
      last.increaseUntil(index)
      last
    } else {
      val component = new ActivityComponent(last.id + 1, this.id, index, 0, true)
      activities += component
      component
    }
  }

  def end(index: Int): Unit = {
    activities.lastOption.foreach(_.end(index))
  }

  override def equals(other: Any): Boolean = other match {
    case that: ActorComponent =>
      (that canEqual this) &&
        id == that.id &&
        name == that.name &&
        activities == that.activities &&
        isLast == that.isLast
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[ActorComponent]

  override def hashCode(): Int = {
    val state = Seq(id, name, activities, isLast)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"ActorComponent($id, $name, $activities, $isLast)"
}
