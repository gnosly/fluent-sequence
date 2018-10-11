package com.gnosly.fluentsequence.view.model.component

import scala.collection.mutable.ListBuffer

class ActivityComponent(val id: Int,
                        val actorId: Int,
                        val fromIndex: Int,
                        var toIndex: Int,
                        var active: Boolean = false,
                        private val _rightPoints: ListBuffer[PointOnTheRight] = ListBuffer(),
                        private val _leftPoints: ListBuffer[PointOnTheLeft] = ListBuffer())
    extends Component {

  def points: Iterable[PointComponent] = _rightPoints ++ _leftPoints

  def rightLoop(signal: AutoSignalComponent): Unit = {
    _rightPoints += PointOnTheRight(signal.currentIndex, signal)
  }

  def right(signal: BiSignalComponent): Unit = {
    _rightPoints += PointOnTheRight(signal.currentIndex, signal)
  }

  def left(signal: BiSignalComponent): Unit = {
    _leftPoints += PointOnTheLeft(signal.currentIndex, signal)
  }

  def end(index: Int): Unit = {
    if (active) {
      increaseUntil(index)
      active = false
    }
  }

  def increaseUntil(index: Int): Unit = {
    toIndex = index
  }

  override def equals(other: Any): Boolean = other match {
    case that: ActivityComponent =>
      (that canEqual this) &&
        rightPoints == that.rightPoints &&
        leftPoints == that.leftPoints &&
        id == that.id &&
        fromIndex == that.fromIndex &&
        toIndex == that.toIndex &&
        active == that.active
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[ActivityComponent]

  override def hashCode: Int = {
    val state = Seq(rightPoints, leftPoints, id, fromIndex, toIndex, active)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"ActivityComponent($id, $fromIndex, $toIndex, $active,$rightPoints, $leftPoints)"

  def rightPoints: Iterable[PointOnTheRight] = _rightPoints
  def leftPoints: Iterable[PointOnTheLeft] = _leftPoints
}

trait PointComponent extends Component {
  def signalComponent: SignalComponent
}

case class PointOnTheRight(id: Int, signal: SignalComponent) extends PointComponent {
  override def signalComponent: SignalComponent = signal
}
case class PointOnTheLeft(id: Int, signal: BiSignalComponent) extends PointComponent {
  override def signalComponent: SignalComponent = signal
}
