package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class MatrixRepresentationGeneratorTest extends FlatSpec with Matchers {


	"generator" should "call own generators " in {
		val matrixRepresentation = generate(EventBook(
			DONE(new Actor(USER(), "user"), "something")
		))

		val userActor2 = Actor2("user", Activity2(0, 1))
		val expected = new Matrix().witha(
			List(userActor2),
			List(AutoSignal("something", 0, userActor2))
		)
		matrixRepresentation shouldBe expected

	}

	def generate(book: EventBook) = {
		val matrix = new Matrix()
		book.toList.foreach(
			t => {
				t.event match {
					case DONE(who, something) => matrix.done(who, something, t.index)
				}
			}
		)
		matrix
	}

	case class Matrix(_actors: mutable.Buffer[Actor2], _signals: mutable.Buffer[AutoSignal]) {

		def this() = {
			this(mutable.Buffer(), mutable.Buffer())
		}


		def witha(actors: List[Actor2], signals: List[AutoSignal]) = {
			_actors ++= actors
			_signals ++= signals
			this
		}

		def done(who: Actor, something: String, when: Int) = {
			val actor = new Actor2(who.name)
			actor.done(when, something)
			_actors += actor
			_signals += AutoSignal(something, when, actor)
		}
	}

	case class AutoSignal(name: String, index: Int, actor: Actor2)

	case class Actor2(name: String, var activity2: Activity2) {
		def done(index: Int, something: String) = {
			activity2 = Activity2(index, index + 1)
		}

		def this(name: String) {
			this(name, null)
		}

	}

	case class Activity2(fromIndex: Int, toIndex: Int)

}
