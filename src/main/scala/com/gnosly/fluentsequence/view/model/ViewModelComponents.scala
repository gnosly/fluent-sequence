package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.core.{CALLED, DONE, EventBook, REPLIED}
import com.gnosly.fluentsequence.view.CorrelationSystem

import scala.collection.mutable

object ViewModelComponentsGenerator {

	def generate(book: EventBook) = {
		val matrix = new ViewModelComponents()
		val list = book.toList
		list.foreach(
			t => {
				t.event match {
					case DONE(who, something) => matrix.done(who, something, t.index)
					case CALLED(who, something, toSomebody) => matrix.called(who, something, toSomebody, t.index)
					case REPLIED(who, something, toSomebody) => matrix.replied(who, something, toSomebody, t.index)
					case other => println(s"WARN ignoring ${other} creating view model")
				}
			}
		)
		matrix.end(list.last.index)
		matrix
	}
}

case class ViewModelComponents(_actors: mutable.HashMap[String, ActorComponent],
															 _signals: mutable.Buffer[SignalComponent]) {

	private val correlationSystem = new CorrelationSystem()

	def this() = {
		this(mutable.HashMap(), mutable.Buffer())
	}

	def done(who: core.Actor, something: String, index: Int): Unit = {
		val actor = createOrGet(who, index)
		val lastActivity = actor.activeUntil(index)
		_signals += AutoSignalComponent(something, index, lastActivity.id, actor)
	}

	def called(who: core.Actor, something: String, toSomebody: core.Actor, index: Int) = {
		val caller = createOrGet(who, index)
		val called = createOrGet(toSomebody, index)
		_signals += caller.link(called, something, index)
	}

	def replied(who: core.Actor, something: String, toSomebody: core.Actor, index: Int) = {
		val replier = createOrGet(who, index)
		val replied = createOrGet(toSomebody, index)
		replier.end(index)
		replied.activeUntil(index)
		_signals += BiSignalComponent(something, index, replier, replied)
	}

	def end(index: Int) = {
		_actors.foreach(a => a._2.end(index))
	}

	private def createOrGet(who: core.Actor, index: Int): ActorComponent = {
		val actor = _actors.getOrElse(who.name, {
			val newActor = new ActorComponent(_actors.size, who.name, index)
			_actors += who.name -> newActor
			newActor
		})

		actor
	}
}

//Components
trait Component

trait SignalComponent extends Component

case class AutoSignalComponent(name: String, index: Int, activityId: Int, actor: ActorComponent) extends SignalComponent

case class BiSignalComponent(name: String, index: Int, fromActor: ActorComponent, toActor: ActorComponent) extends SignalComponent

case class ActorComponent(val column: Int, name: String, var activities: mutable.Buffer[ActivityComponent]) extends Component {
	def link(called: ActorComponent, something: String, index: Int): SignalComponent = {
		this.activeUntil(index)
		called.activeUntil(index)
		BiSignalComponent(something, index, this, called)
	}

	def activeUntil(index: Int): ActivityComponent = {
		val last = activities.last
		if (last.active) {
			last.increaseUntil(index)
			last
		}
		else {
			val component = ActivityComponent(last.id + 1, index, 0, true)
			activities += component
			component
		}
	}

	def this(column: Int, name: String, activity: ActivityComponent) {
		this(column, name, mutable.Buffer(activity))
	}

	def this(column: Int, name: String, fromIndex: Int) {
		this(column, name, ActivityComponent(0, fromIndex, 0, true))
	}

	def end(index: Int) = {
		activities.last.end(index)
	}
}

case class ActivityComponent(id: Int, fromIndex: Int, var toIndex: Int, var active: Boolean = false) extends Component {
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
