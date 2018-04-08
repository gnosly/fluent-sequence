package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

case class ActivityComponent(id: Int, fromIndex: Int, var toIndex: Int, var active: Boolean = false) extends Component {


	val rightPoints: mutable.TreeMap[Int, SignalComponent] = mutable.TreeMap()
	val leftPoints: mutable.TreeMap[Int, SignalComponent] = mutable.TreeMap()

	def right(signal: SignalComponent) = rightPoints.put(rightPoints.size, signal)
	def left(signal: BiSignalComponent) = leftPoints.put(leftPoints.size, signal)

	def end(index: Int): Unit = {
		if (active) {
			increaseUntil(index)
			active = false
		}
	}

	def increaseUntil(index: Int): Unit = {
		toIndex = index
	}
}
