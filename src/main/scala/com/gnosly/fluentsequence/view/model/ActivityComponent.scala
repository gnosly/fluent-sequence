package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

class ActivityComponent(val id: Int,
												val fromIndex: Int,
												var toIndex: Int,
												var active: Boolean = false) extends Component {


	val rightPoints: mutable.TreeMap[Int, ActivityPoint] = mutable.TreeMap()
	val leftPoints: mutable.TreeMap[Int, ActivityPoint] = mutable.TreeMap()

	def right(signal: SignalComponent): Unit = {
		rightPoints.put(rightPoints.size, ActivityPoint(signal.currentIndex(), signal, "right"))
	}

	def left(signal: BiSignalComponent): Unit = {
		leftPoints.put(leftPoints.size, ActivityPoint(signal.currentIndex(), signal, "left"))
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


	override def toString = s"ActivityComponent($rightPoints, $leftPoints, $id, $fromIndex, $toIndex, $active)"
}

case class ActivityPoint(id: Int, signalComponent: SignalComponent, direction: String)