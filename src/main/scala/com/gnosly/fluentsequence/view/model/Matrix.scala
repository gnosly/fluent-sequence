package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.core.{CALLED, DONE, EventBook, REPLIED}

import scala.collection.mutable

object MatrixGenerator{

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
}

case class Matrix(_actors: mutable.HashMap[String, MatrixActor], _signals: mutable.Buffer[Signal]) {

	def this() = {
		this(mutable.HashMap(), mutable.Buffer())
	}

	def witha(actors: Map[String, MatrixActor], signals: List[Signal]) = {
		_actors ++= actors
		_signals ++= signals
		this
	}

	def done(who: core.Actor, something: String, index: Int) = {
		val actor = createOrGet(who, index)
		actor.activeUntil(index)
		_signals += AutoSignal(something, index, actor)
	}


	def called(who: core.Actor, something: String, toSomebody: core.Actor, index: Int) = {
		val caller = createOrGet(who, index)
		val called = createOrGet(toSomebody, index)
		caller.activeUntil(index)
		called.activeUntil(index)
		_signals += BiSignal(something, index, caller, called)
	}


	def replied(who: core.Actor, something: String, toSomebody: core.Actor, index: Int) = {
		val replier = createOrGet(who, index)
		val replied = createOrGet(toSomebody, index)
		replier.end(index)
		replied.activeUntil(index)
		_signals += BiSignal(something, index, replier, replied)
	}

	private def createOrGet(who: core.Actor, index: Int): MatrixActor = {
		val actor = _actors.getOrElse(who.name, {
			val newActor = new MatrixActor(who.name, index)
			_actors += who.name -> newActor
			newActor
		})

		actor
	}

	def end(index: Int) = {
		_actors.foreach(a => a._2.end(index))
	}
}

trait Signal

case class AutoSignal(name: String, index: Int, actor: MatrixActor) extends Signal

case class BiSignal(name: String, index: Int, fromActor: MatrixActor, toActor: MatrixActor) extends Signal

case class MatrixActor(name: String, var activities: mutable.Buffer[Activity]) {

	def this(name: String, activity: Activity) {
		this(name, mutable.Buffer(activity))
	}

	def this(name: String, fromIndex: Int) {
		this(name, Activity(fromIndex, 0, true))
	}

	def activeUntil(index: Int) = {
		val last = activities.last
		if(last.active)
			last.increaseUntil(index)
		else
			activities += Activity(index, 0, true)
	}

	def end(index: Int) = {
		activities.last.end(index)
	}
}

case class Activity(fromIndex: Int, var toIndex: Int, var active: Boolean = false) {
	def end(index: Int) = {
		if (active) {
			increaseUntil(index)
			active = false
		}
	}

	def increaseUntil(index: Int) = {
		toIndex = index
	}
}

