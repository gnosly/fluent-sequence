package com.gnosly.api

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

		def and(flow: SequenceFlow): SequenceFlow = {
			eventBook.track(flow.eventBook :: Nil)
			this
		}

		override def toEventBook: EventBook = eventBook
	}


	class Actor(val name: String, entity: String = "ACTOR") {


		def check(condition: String): SequenceFlow = ???

		def stop(): SequenceFlow = ???

		def fire(event: String): SequenceFlow = ???

		def launch(tracking: Sequence): SequenceFlow = ???

		def does(sequence: Sequence): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name STARTED_NEW_SEQUENCE ${sequence.name}"
			eventBook.track(event)

			eventBook.track(sequence.eventBook :: Nil)
			new SequenceFlow(event, eventBook, this)
		}

		def does(action: String): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name DOES $action"
			eventBook.track(event)
			new SequenceFlow(event, eventBook, this)
		}

		def call(action: String, actor: Actor): SequenceFlow = {
			val eventBook = new EventBook()
			val event = s"$entity $name CALLED $action TO ${actor.name}"
			eventBook.track(event)
			new SequenceFlow(event, eventBook, this)
		}

		def reply(action: String, actor: Actor): SequenceFlow = ???

		def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
	}

	class User(role: String) extends Actor(name = role, entity = "USER") {}

}
