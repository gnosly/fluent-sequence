package com.gnosly.fluentsequence.view.model.component

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class ActivityComponent(val id: Int,
                        val actorId: Int,
                        val fromIndex: Int,
                        var toIndex: Int,
                        var active: Boolean = false,
                        private val _rightPoints: ListBuffer[RightPoint] = ListBuffer(),
                        private val _leftPoints: ListBuffer[LeftPoint] = ListBuffer())
    extends Component {
  def isFirst: Boolean = id == 0

  def points: Iterable[ActivityPoint] = _rightPoints ++ _leftPoints

  def rightLoop(signal: AutoSignalComponent): Unit = {
    val pointId = rightPoints.size
    _rightPoints += ActivityPointLoopOnTheRight(signal.currentIndex, signal)
  }

  def right(signal: BiSignalComponent): Unit = {
    val pointId = rightPoints.size
    _rightPoints += ActivityPointForBiSignalOnTheRight(signal.currentIndex, signal)
  }

  def left(signal: BiSignalComponent): Unit = {
    val pointId = _leftPoints.size
    _leftPoints += ActivityPointForBiSignalOnTheLeft(signal.currentIndex, signal)
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

  def rightPoints: Iterable[RightPoint] = _rightPoints
  def leftPoints: Iterable[LeftPoint] = _leftPoints
}

trait ActivityPoint {
  def signalComponent: SignalComponent
}

trait RightPoint extends ActivityPoint {}
trait LeftPoint extends ActivityPoint {}
case class ActivityPointLoopOnTheRight(id: Int, signal: AutoSignalComponent) extends RightPoint {
  override def signalComponent: AutoSignalComponent = signal
}
//FIXME which value givs this ActivityPointForBiSignalOnTheRight?
case class ActivityPointForBiSignalOnTheRight(id: Int, signal: BiSignalComponent) extends RightPoint {
  override def signalComponent: BiSignalComponent = signal
}
case class ActivityPointForBiSignalOnTheLeft(id: Int, signal: BiSignalComponent) extends LeftPoint {
  override def signalComponent: BiSignalComponent = signal
}
