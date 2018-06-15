package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.core._

import scala.collection.mutable

object ViewModelComponentsFactory {

	def createFrom(book: EventBook) = {
		val viewModel = new ViewModelComponents()
		val list = book.toList
		list.foreach(
			t => {
				t.event match {
					case DONE(who, something) => viewModel.done(who, something, t.index)
					case CALLED(who, something, toSomebody) => viewModel.called(who, something, toSomebody, t.index)
					case REPLIED(who, something, toSomebody) => viewModel.replied(who, something, toSomebody, t.index)
					case SEQUENCE_STARTED(name) => viewModel.sequenceStarted(name)
					case other => println(s"WARN ignoring ${other} creating view model")
				}
			}
		)
		viewModel.end(list.last.index)
		viewModel
	}
}

case class ViewModelComponents(_actors: mutable.HashMap[String, ActorComponent] = mutable.HashMap()) {
	var sequenceName = ""
	var lastIndex= 0

	def sequenceStarted(name: String): Unit = {
		sequenceName = name
	}

	def done(who: core.Actor, something: String, index: Int): Unit = {
		val actor = createOrGet(who, index)
		/*_signals += */ actor.done(something, index)
	}

	def called(who: core.Actor, something: String, toSomebody: core.Actor, index: Int) = {
		val caller = createOrGet(who, index)
		val called = createOrGet(toSomebody, index)
		/*_signals += */ caller.link(called, something, index)
	}

	def replied(who: core.Actor, something: String, toSomebody: core.Actor, index: Int) = {
		val replier = createOrGet(who, index)
		val replied = createOrGet(toSomebody, index)
		/*_signals += */ replier.link(replied, something, index)
		replier.end(index)
	}

	def lastActorId(): Int ={
		_actors.size-1
	}

	private def createOrGet(who: core.Actor, index: Int): ActorComponent = {
		val actor = _actors.getOrElse(who.name, {
			val newActor = new ActorComponent(_actors.size, who.name)
			_actors += who.name -> newActor
			newActor
		})

		actor
	}

	def end(index: Int) = {
		lastIndex = index
		_actors.foreach(a => a._2.end(index))
	}
}