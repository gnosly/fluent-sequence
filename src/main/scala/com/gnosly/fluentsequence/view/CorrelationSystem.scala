package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent, AutoSignalComponent}

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Seq}

class CorrelationSystem {

	val actorPoints: mutable.Map[ActorComponent, ActorPoints] = mutable.HashMap[ActorComponent, ActorPoints]()
	val activityPoints: mutable.Map[ActivityComponent, ActivityPoints] = mutable.HashMap[ActivityComponent,ActivityPoints]()
	val autoSignalPoints: ListBuffer[AutoSignalPoints] = ListBuffer[AutoSignalPoints]()

	def add(actorComponent: ActorComponent): Unit = {
		actorPoints += actorComponent -> ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10)))
	}

	def add(signal: AutoSignalComponent): Unit = {
		val actor = signal.actor
		val index = signal.index
		val activity = actor.activities(signal.activityId)
		val activityPoint = activityPoints(activity)
		autoSignalPoints += AutoSignalPoints(activityPoint.rights(index))
		//forse mi conviene tenere il max
	}
}

case class AutoSignalPoints(start: Point) {
}


case class ActorPoints(bottomLeft: Point, bottomRight: Point) {
	def bottomMiddle(): Point = {
		Point(bottomRight.x - bottomLeft.x, bottomLeft.y)
	}
}

case class ActivityPoints(bottomLeft: Point, bottomRight: Point, rightPoints: Seq[Point]) {
	def rights(index: Int): Point = rightPoints(index)
}

case class Point(x: Pixel, y: Pixel)

case class Pixel(value: Int) {
	def -(other: Pixel): Pixel = Pixel(this.value - other.value)
}
