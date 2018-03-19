package com.gnosly.fluentsequence.view.model

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable
import scala.collection.mutable._

class CorrelationSystemTest extends FlatSpec with Matchers {

	val system = CorrelationSystem()

	"System" should "pre-render single actor" in {
		val actorComponent = ActorComponent(0, "user", ListBuffer[ActivityComponent]())

		system.add(actorComponent)

		system.actorPoints shouldBe Seq(ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10))))
	}

	it should "pre-render single actor with a auto sequence" in {
		val actorComponent = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))
		val autoSignalComponent = AutoSignalComponent("something", 0, 0, actorComponent)

		system.add(actorComponent)
		system.add(autoSignalComponent)

		system.actorPoints shouldBe Seq(ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10))))
		system.autoSignalPoints shouldBe Seq(ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10))))
	}


	case class Point(x: Pixel, y: Pixel)

	case class Pixel(value: Int) {
		def -(other: Pixel): Pixel = Pixel(this.value - other.value)
	}

	case class CorrelationSystem() {
		 val actorPoints: mutable.Map[ActorComponent, ActorPoints] = mutable.HashMap[ActorComponent, ActorPoints]()
		 val autoSignalPoints: ListBuffer[AutoSignalPoints] = ListBuffer[AutoSignalPoints]()

		def add(actorComponent: ActorComponent): Unit = {
			actorPoints += actorComponent -> ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10)))
		}

		def add(signal: AutoSignalComponent): Unit = {
			val actor = signal.actor
			val actorPoint = actorPoints(actor)
			val index = signal.index
			val activity = actor.activities(signal.activityId)

			autoSignalPoints += AutoSignalPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10)), Point(Pixel(40), Pixel(10)))
		}

	}

	case class AutoSignalPoints(startY: Point, endY: Point, endX:Point) {
	}


	case class ActorPoints(bottomLeft: Point, bottomRight: Point) {
		def bottomMiddle(): Point = {
			Point(bottomRight.x - bottomLeft.x, bottomLeft.y)
		}
	}

	case class ActivityPoints(bottomLeft: Point, bottomRight: Point) {
		def bottomMiddle(): Point = {
			Point(bottomRight.x - bottomLeft.x, bottomLeft.y)
		}
	}

}
