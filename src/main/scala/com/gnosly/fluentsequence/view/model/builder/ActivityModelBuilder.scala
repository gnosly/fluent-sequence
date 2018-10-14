package com.gnosly.fluentsequence.view.model.builder

import com.gnosly.fluentsequence.view.model.ViewModels._

import scala.collection.mutable.ListBuffer

class ActivityModelBuilder(val id: Int,
                           val actorId: Int,
                           val fromIndex: Int,
                           var toIndex: Int,
                           var active: Boolean = false,
                           private val _rightPoints: ListBuffer[PointOnTheRight] = ListBuffer(),
                           private val _leftPoints: ListBuffer[PointOnTheLeft] = ListBuffer()) {

  def points: Iterable[PointModel] = _rightPoints ++ _leftPoints

  def rightLoop(signal: AutoSignalModel): Unit = {
    _rightPoints += PointOnTheRight(signal.currentIndex, signal)
  }

  def right(signal: BiSignalModel): Unit = {
    _rightPoints += PointOnTheRight(signal.currentIndex, signal)
  }

  def left(signal: BiSignalModel): Unit = {
    _leftPoints += PointOnTheLeft(signal.currentIndex, signal)
  }

  def build(index: Int): ActivityModel = {
    if (active) {
      increaseUntil(index)
      active = false
    }

    ActivityModel(id, actorId, fromIndex, toIndex)
  }

  def increaseUntil(index: Int): Unit = {
    toIndex = index
  }

  override def equals(other: Any): Boolean = false

  def canEqual(other: Any): Boolean = other.isInstanceOf[ActivityModelBuilder]

  override def hashCode: Int = {
    val state = Seq(rightPoints, leftPoints, id, fromIndex, toIndex, active)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"ActivityComponent($id, $fromIndex, $toIndex, $active,$rightPoints, $leftPoints)"

  def rightPoints: Iterable[PointOnTheRight] = _rightPoints
  def leftPoints: Iterable[PointOnTheLeft] = _leftPoints
}
