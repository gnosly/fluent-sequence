package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable._

class CorrelationSystemTest extends FlatSpec with Matchers {

	val system = new CorrelationSystem()

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
		system.autoSignalPoints shouldBe Seq(AutoSignalPoints(Point(Pixel(0),Pixel(0))))
	}





}
