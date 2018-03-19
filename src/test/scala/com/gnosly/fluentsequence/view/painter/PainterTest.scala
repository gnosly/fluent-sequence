package com.gnosly.fluentsequence.view.painter

import com.gnosly.fluentsequence.view.model.{ActivityComponent, ActorComponent}
import org.scalatest.{FlatSpec, Matchers}

class PainterTest extends FlatSpec with Matchers {

	"ActorPainter" should "calculare intrisic dimension of points" in {
		val actorComponent = new ActorComponent(0, "user", ActivityComponent(0, 0, 1))

		new FixedWidthActorPainter().calculate(actorComponent) shouldBe
			ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10)))
	}

	"System" should "link Actor with Activity" in {

		val actorPoints = ActorPoints(Point(Pixel(0), Pixel(10)), Point(Pixel(40), Pixel(10)))
		val activityPoints = ActivityPoints(dx = List(Point(Pixel(1), Pixel(1))))

		CorrelationSystem().link(activityPoints.top, actorPoints.bottomMiddle())
	}

	class FixedWidthActorPainter() {
		def calculate(actorComponent: ActorComponent): ActorPoints = ???

	}

	case class ActorPoints(bottomLeft: Point, bottomRight: Point) {
		def bottomMiddle(): Point = {
			Point(bottomRight.x - bottomLeft.x, bottomLeft.y)
		}
	}

	case class Point(x: Pixel, y: Pixel)
	case class Incognite(name:String)

	case class Pixel(value: Int) {
		def -(other: Pixel): Pixel = Pixel(this.value - other.value)
	}

	case class CorrelationSystem() {
		def link(incognite: Incognite, point: Point): Any = ???
	}

	case class ActivityPoints(top:Incognite = Incognite("top"), bottom:Incognite = Incognite("bottom"), dx: List[Point])

}
