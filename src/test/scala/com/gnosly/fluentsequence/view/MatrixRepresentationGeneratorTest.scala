package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class MatrixRepresentationGeneratorTest extends FlatSpec with Matchers {


	"generator" should "create matrix with DOES " in {
		val matrixRepresentation = generate(EventBook(
			DONE(new Actor(USER(), "user"), "something"),
			DONE(new Actor(USER(), "user"), "something else")
		))

		val userActor2 = Actor2("user", Activity2(0, 2))
		val expected = new Matrix().witha(
			Map("user" -> userActor2),
			List(AutoSignal("something", 0, userActor2), AutoSignal("something else", 1, userActor2) )
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with CALL " in {
		val matrixRepresentation = generate(EventBook(
			CALLED(new Actor(USER(), "user"), "call", Actor(SEQUENCE_ACTOR(),"system"))
		))

		val userActor2 = Actor2("user", Activity2(0, 1))
		val systemActor2 = Actor2("system", Activity2(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(BiSignal2("call", 0, userActor2,systemActor2))
		)
		matrixRepresentation shouldBe expected

	}

	def generate(book: EventBook) = {
		val matrix = new Matrix()
		book.toList.foreach(
			t => {
				t.event match {
					case DONE(who, something) => matrix.done(who, something, t.index)
					case CALLED(who, something,toSomebody) => matrix.called(who, something, toSomebody, t.index)
				}
			}
		)
		matrix
	}

	case class Matrix(_actors: mutable.HashMap[String,Actor2], _signals: mutable.Buffer[Signal2]) {
		def this() = {
			this(mutable.HashMap(), mutable.Buffer())
		}


		def witha(actors: Map[String, Actor2], signals: List[Signal2]) = {
			_actors ++= actors
			_signals ++= signals
			this
		}


		def done(who: Actor, something: String, when: Int) = {
			val actor = createOrGet(who)
			actor.done(when)
			_signals += AutoSignal(something, when, actor)
		}

		def called(who: Actor, something: String, toSomebody: Actor, index: Int) = {
			val caller = createOrGet(who)
			val called = createOrGet(toSomebody)
			caller.done(index)
			called.done(index)
			_signals += BiSignal2(something, index,caller, called)
		}

		private def createOrGet(who: Actor):Actor2 = {
			val actor = _actors.getOrElse(who.name, {
				val newActor = new Actor2(who.name)
				_actors += who.name -> newActor
				newActor
			})

			 actor
		}
	}

	trait Signal2
	case class AutoSignal(name: String, index: Int, actor: Actor2) extends Signal2
	case class BiSignal2(name: String, index: Int, fromActor: Actor2,toActor: Actor2) extends Signal2

	case class Actor2(name: String, var activity2: Activity2) {
		def done(index: Int) = {
			activity2.increaseUntil(index)
		}

		def this(name: String) {
			this(name, Activity2(0,0))
		}

	}

	case class Activity2(fromIndex: Int, var toIndex: Int) {
		def increaseUntil(index: Int) = {
			toIndex=index+1
		}

	}

}
