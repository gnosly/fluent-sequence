package com.gnosly.fluentsequence.api

import java.io.PrintWriter

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.ConsolePrinter

object FluentSequence {


	def to(actor: FluentActor): FluentActor = ???

	case class Sequence(name: String) extends EventBookable {
		val eventBook = new EventBook()

		def printToConsole() = {
			ConsolePrinter.print(this.toEventBook)
				.on(new PrintWriter(System.out))
		}

		def startWith(flow: Seq[SequenceFlow]): Sequence = {
			eventBook.track(SEQUENCE_STARTED(name))
			eventBook.track(flow.map(_.toEventBook))
			this
		}

		override def toEventBook: EventBook = eventBook
	}

	class SequenceFlow(name: String, val eventBook: EventBook, actorDoingSequence: FluentActor) extends EventBookable {

		def inCase(statement: String, flow: SequenceFlow): SequenceFlow = ???

		def and(): ActorContinueSequenceFlow = {
			new ActorContinueSequenceFlow(this, actorDoingSequence)
		}

		override def toEventBook: EventBook = eventBook

		class ActorContinueSequenceFlow(sequenceFlow: SequenceFlow, val actor: FluentActor) extends Actorable {
			override def check(condition: String): SequenceFlow = ???

			override def stop(): SequenceFlow = ???

			override def fire(event: String): SequenceFlow = ???

			override def launch(tracking: Sequence): SequenceFlow = ???

			override def does(sequence: Sequence): SequenceFlow = ???

			override def does(action: String): SequenceFlow = {
				sequenceFlow.eventBook.track(DONE(actor, action))
				sequenceFlow
			}

			override def call(action: String, actor: FluentActor): SequenceFlow = ???

			override def reply(action: String, actor: FluentActor): SequenceFlow = ???

			override def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
		}

	}

	class FluentActor(name: String, val entity: ActorType = SEQUENCE_ACTOR_TYPE())
		extends Actor(entity, name)
			with Actorable {

		override def does(sequence: Sequence): SequenceFlow = {
			val eventBook = new EventBook()
			eventBook.track(NEW_SEQUENCE_SCHEDULED(this, sequence.name))

			eventBook.track(sequence.eventBook :: Nil)
			new SequenceFlow(s"$name ${sequence.name}", eventBook, this)
		}

		override def does(action: String): SequenceFlow = {
			val eventBook = new EventBook()
			eventBook.track(DONE(this, action))
			new SequenceFlow(s"$name $action", eventBook, this)
		}

		override def call(action: String, actor: FluentActor): SequenceFlow = {
			val eventBook = new EventBook()
			eventBook.track(CALLED(this, action, actor))
			new SequenceFlow(s"$name $action to ${actor.name}", eventBook, this)
		}

		override def reply(action: String, toActor: FluentActor): SequenceFlow = {
			val eventBook = new EventBook()
			eventBook.track(REPLIED(this, action, toActor))
			new SequenceFlow(s"$name replied $action to ${toActor.name}", eventBook, this)
		}

		override def stop(): SequenceFlow = ???

		override def check(condition: String): SequenceFlow = ???

		override def fire(event: String): SequenceFlow = ???

		override def launch(tracking: Sequence): SequenceFlow = ???

		override def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
	}

	class User(role: String) extends FluentActor(name = role, entity = USER_TYPE()) {}

}
