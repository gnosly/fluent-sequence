package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.core.EventBook

object FluentSequence {


	def to(actor: Actor): Actor = ???

	case class Sequence(name: String) extends EventBookable {
		val eventBook = new EventBook()

		def startWith(flow: Seq[SequenceFlow]): Sequence = {
			eventBook.track(s"SEQUENCE $name STARTED")
			eventBook.track(flow.map(_.toEventBook))
			this
		}

		override def toEventBook: EventBook = eventBook
	}

	class SequenceFlow(name: String, val eventBook: EventBook, actorDoingSequence: Actor) extends EventBookable {

		def inCase(statement: String, flow: SequenceFlow): SequenceFlow = ???

		def and(): ActorContinueSequenceFlow = {
			new ActorContinueSequenceFlow(this, actorDoingSequence)
		}

		override def toEventBook: EventBook = eventBook

		class ActorContinueSequenceFlow(sequenceFlow: SequenceFlow, val actor: Actor) extends Actorable {
			override def check(condition: String): SequenceFlow = ???

			override def stop(): SequenceFlow = ???

			override def fire(event: String): SequenceFlow = ???

			override def launch(tracking: Sequence): SequenceFlow = ???

			override def does(sequence: Sequence): SequenceFlow = ???

			override def does(action: String): SequenceFlow = {
				sequenceFlow.eventBook.track(s"${actor.entity} ${actor.name} DOES $action")
				sequenceFlow
			}

			override def call(action: String, actor: Actor): SequenceFlow = ???

			override def reply(action: String, actor: Actor): SequenceFlow = ???

			override def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
		}

	}


	class Actor(val name: String, val entity: String = "ACTOR") extends Actorable {

		override def does(sequence: Sequence): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name STARTED_NEW_SEQUENCE ${sequence.name}"
			eventBook.track(event)

			eventBook.track(sequence.eventBook :: Nil)
			new SequenceFlow(event, eventBook, this)
		}

		override def does(action: String): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name DOES $action"
			eventBook.track(event)
			new SequenceFlow(event, eventBook, this)
		}

		override def call(action: String, actor: Actor): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name CALLED $action TO ${actor.name}"
			eventBook.track(event)
			new SequenceFlow(event, eventBook, this)
		}

		override def reply(action: String, toActor: Actor): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name REPLIED $action TO ${toActor.name}"
			eventBook.track(event)
			new SequenceFlow(event, eventBook, this)
		}

		override def stop(): SequenceFlow = ???

		override def check(condition: String): SequenceFlow = ???

		override def fire(event: String): SequenceFlow = ???

		override def launch(tracking: Sequence): SequenceFlow = ???

		override def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
	}

	class User(role: String) extends Actor(name = role, entity = "USER") {}

	trait Actorable {

		def check(condition: String): SequenceFlow

		def stop(): SequenceFlow

		def fire(event: String): SequenceFlow

		def launch(tracking: Sequence): SequenceFlow

		def does(sequence: Sequence): SequenceFlow

		def does(action: String): SequenceFlow

		def call(action: String, actor: Actor): SequenceFlow

		def reply(action: String, actor: Actor): SequenceFlow

		def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow
	}

}
