package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

class ActivityComponent(val id: Int,
												val fromIndex: Int,
												var toIndex: Int,
												var active: Boolean = false,
												val rightPoints: mutable.TreeMap[Int, ActivityPoint] = mutable.TreeMap(),
												val leftPoints: mutable.TreeMap[Int, ActivityPoint] = mutable.TreeMap()) extends Component {

	def right(signal: SignalComponent): Unit = {
		val pointId = rightPoints.size
		rightPoints.put(pointId, ActivityPoint(signal.currentIndex(), signal, "right"))
	}

	def left(signal: BiSignalComponent): Unit = {
		val pointId = leftPoints.size
		leftPoints.put(pointId, ActivityPoint(signal.currentIndex(), signal, "left"))
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


	def canEqual(other: Any): Boolean = other.isInstanceOf[ActivityComponent]

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

	override def hashCode(): Int = {
		val state = Seq(rightPoints, leftPoints, id, fromIndex, toIndex, active)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}


	override def toString = s"ActivityComponent($id, $fromIndex, $toIndex, $active,$rightPoints, $leftPoints)"
}

case class ActivityPoint(id: Int, signalComponent: SignalComponent, direction: String)