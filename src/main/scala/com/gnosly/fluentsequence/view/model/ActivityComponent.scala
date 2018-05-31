package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

class ActivityComponent(val id: Int,
												val actorId:Int,
												val fromIndex: Int,
												var toIndex: Int,
												var active: Boolean = false,
												val rightPoints: mutable.TreeMap[Int, RightPoint] = mutable.TreeMap(),
												val leftPoints: mutable.TreeMap[Int, LeftPoint] = mutable.TreeMap()) extends Component {
	def points() = rightPoints ++ leftPoints


	def rightLoop(signal: AutoSignalComponent): Unit = {
		val pointId = rightPoints.size
		rightPoints.put(pointId, ActivityPointLoopOnTheRight(signal.currentIndex(), signal))
	}

	def right(signal: BiSignalComponent): Unit = {
		val pointId = rightPoints.size
		rightPoints.put(pointId, ActivityPointForBiSignalOnTheRight(signal.currentIndex(), signal))
	}

	def left(signal: BiSignalComponent): Unit = {
		val pointId = leftPoints.size
		leftPoints.put(pointId, ActivityPointForBiSignalOnTheLeft(signal.currentIndex(), signal))
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

trait ActivityPoint
trait RightPoint extends ActivityPoint{
	def signalComponent():SignalComponent
}
trait LeftPoint extends ActivityPoint{
	def signalComponent():BiSignalComponent
}
case class ActivityPointLoopOnTheRight(id: Int, signal: AutoSignalComponent) extends RightPoint {
	override def signalComponent(): AutoSignalComponent = signal
}
case class ActivityPointForBiSignalOnTheRight(id: Int, signal: BiSignalComponent) extends RightPoint {
	override def signalComponent(): BiSignalComponent = signal
}
case class ActivityPointForBiSignalOnTheLeft(id: Int, signal: BiSignalComponent) extends LeftPoint {
	override def signalComponent(): BiSignalComponent = signal
}