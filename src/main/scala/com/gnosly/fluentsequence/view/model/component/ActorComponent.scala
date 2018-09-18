package com.gnosly.fluentsequence.view.model.component

import scala.collection.mutable

class ActorComponent(val id: Int,
                     val name: String,
                     val activities: mutable.Buffer[ActivityComponent] = mutable.Buffer[ActivityComponent]())
    extends Component {

  def done(something: String, index: Int): SignalComponent = {
    val lastActivity = this.activeUntil(index)
    val autoSignal = new AutoSignalComponent(something, index, this.id, lastActivity.id)
    lastActivity.rightLoop(autoSignal)
    autoSignal
  }

  def link(called: ActorComponent, something: String, index: Int): SignalComponent = {
    val lastCallerActivity = this.activeUntil(index)
    val lastCalledActivity = called.activeUntil(index)
    val signal =
      new BiSignalComponent(something, index, this.id, lastCallerActivity.id, called.id, lastCalledActivity.id)
    if (signal.leftToRight()) {
      lastCallerActivity.right(signal)
      lastCalledActivity.left(signal)
    } else {
      lastCallerActivity.left(signal)
      lastCalledActivity.right(signal)
    }
    signal
  }

  def end(index: Int): Unit = {
    activities.last.end(index)
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

  override def equals(other: Any): Boolean = other match {
    case that: ActorComponent =>
      (that canEqual this) &&
        id == that.id &&
        name == that.name &&
        activities == that.activities
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[ActorComponent]

  override def hashCode(): Int = {
    val state = Seq(id, name, activities)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"ActorComponent($id, $name, $activities)"
}
