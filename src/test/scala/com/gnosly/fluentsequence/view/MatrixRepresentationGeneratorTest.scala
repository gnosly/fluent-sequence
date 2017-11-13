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

		val userActor2 = Actor2("user", Activity2(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> userActor2),
			List(AutoSignal("something", 0, userActor2), AutoSignal("something else", 1, userActor2))
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with CALL " in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrixRepresentation = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
		))

		val userActor2 = Actor2("user", Activity2(0, 1))
		val systemActor2 = Actor2("system", Activity2(0, 1))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(BiSignal2("call", 0, userActor2, systemActor2), AutoSignal("something", 1, systemActor2))
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with REPLY " in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrixRepresentation = generate(EventBook(
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something"),
			REPLIED(systemActor, "response", userActor)
		))

		val userActor2 = Actor2("user", Activity2(0, 2))
		val systemActor2 = Actor2("system", Activity2(0, 2))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(
				BiSignal2("call", 0, userActor2, systemActor2),
				AutoSignal("something", 1, systemActor2),
				BiSignal2("response", 2, systemActor2, userActor2))
		)
		matrixRepresentation shouldBe expected

	}

	it should "create matrix with mixed events" in {
		val systemActor = Actor(SEQUENCE_ACTOR(), "system")
		val userActor = new Actor(USER(), "user")

		val matrixRepresentation = generate(EventBook(
			DONE(userActor, "something"),
			CALLED(userActor, "call", systemActor),
			DONE(systemActor, "something else"),
			REPLIED(systemActor, "response", userActor),
			DONE(userActor, "something end"),
		))

		val userActor2 = Actor2("user", Activity2(0, 4))
		val systemActor2 = Actor2("system", Activity2(1, 3))
		val expected = new Matrix().witha(
			Map("user" -> userActor2, "system" -> systemActor2),
			List(
				AutoSignal("something", 0, userActor2),
				BiSignal2("call", 1, userActor2, systemActor2),
				AutoSignal("something else", 2, systemActor2),
				BiSignal2("response", 3, systemActor2, userActor2),
				AutoSignal("something end", 4, userActor2),
			)
		)
		matrixRepresentation shouldBe expected

	}

	def generate(book: EventBook) = {
		val matrix = new Matrix()
		val list = book.toList
		list.foreach(
			t => {
				t.event match {
					case DONE(who, something) => matrix.done(who, something, t.index)
					case CALLED(who, something, toSomebody) => matrix.called(who, something, toSomebody, t.index)
					case REPLIED(who, something, toSomebody) => matrix.replied(who, something, toSomebody, t.index)
				}
			}
		)
		matrix.end(list.last.index)
		matrix
	}

	trait Signal2

	case class Matrix(_actors: mutable.HashMap[String, Actor2], _signals: mutable.Buffer[Signal2]) {

		def this() = {
			this(mutable.HashMap(), mutable.Buffer())
		}

		def witha(actors: Map[String, Actor2], signals: List[Signal2]) = {
			_actors ++= actors
			_signals ++= signals
			this
		}

		def done(who: Actor, something: String, index: Int) = {
			val actor = createOrGet(who, index)
			actor.activeUntil(index)
			_signals += AutoSignal(something, index, actor)
		}


		def called(who: Actor, something: String, toSomebody: Actor, index: Int) = {
			val caller = createOrGet(who, index)
			val called = createOrGet(toSomebody, index)
			caller.activeUntil(index)
			called.activeUntil(index)
			_signals += BiSignal2(something, index, caller, called)
		}


		def replied(who: Actor, something: String, toSomebody: Actor, index: Int) = {
			val replier = createOrGet(who, index)
			val replied = createOrGet(toSomebody, index)
			replier.end(index)
			replied.activeUntil(index)
			_signals += BiSignal2(something, index, replier, replied)
		}

		def end(index: Int) = {
			_actors.foreach(a => a._2.end(index))
		}

		private def createOrGet(who: Actor, index:Int): Actor2 = {
			val actor = _actors.getOrElse(who.name, {
				val newActor = new Actor2(who.name, index)
				_actors += who.name -> newActor
				newActor
			})

			actor
		}
	}

	case class AutoSignal(name: String, index: Int, actor: Actor2) extends Signal2

	case class BiSignal2(name: String, index: Int, fromActor: Actor2, toActor: Actor2) extends Signal2

	case class Actor2(name: String, var activity2: Activity2) {
		def this(name: String, fromIndex:Int) {
			this(name, Activity2(fromIndex, 0, true))
		}

		def activeUntil(index: Int) = {
			activity2.increaseUntil(index)
		}

		def end(index: Int) = {
			activity2.end(index)
		}

	}

	case class Activity2(fromIndex: Int, var toIndex: Int, var active:Boolean = false) {
		def end(index: Int) = {
			if(active){
				increaseUntil(index)
				active = false
			}
		}


		def increaseUntil(index: Int) = {
			toIndex = index
		}

	}

}
