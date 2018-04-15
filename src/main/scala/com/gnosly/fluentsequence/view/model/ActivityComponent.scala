package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

case class ActivityComponent(id: Int,
														 fromIndex: Int,
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
}

case class ActivityPoint(id: Int, signalComponent: SignalComponent, direction:String)