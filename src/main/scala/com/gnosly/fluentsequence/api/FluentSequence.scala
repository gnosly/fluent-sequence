package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthViewer
import com.gnosly.fluentsequence.view.svg.SvgViewer

object FluentSequence {
  val fixedWidthViewer = new FixedWidthViewer
  val svgViewer = new SvgViewer

  def to(actor: FluentActor): FluentActor = ???

  def inCase(statement: String, flows: Seq[SequenceFlow]): AlternativeSequence = {
    AlternativeSequence(statement, flows)
  }

  case class AlternativeSequence(statement: String, flows: Seq[SequenceFlow]) extends EventBookable {
    override def toEventBook: EventBook = {
      EventBook()
        .track(ALTERNATIVE_STARTED(statement))
        .track(flows.map(_.toEventBook))
        .track(ALTERNATIVE_ENDED(statement))
    }
  }

  case class Sequence(name: String) extends EventBookable {

    val eventBook = EventBook()

    def printToConsole() = {
      println(fixedWidthViewer.view(this.toEventBook))
    }

    override def toEventBook: EventBook = eventBook

    def printToSvg() = println(svgViewer.view(this.toEventBook))

    def startWith(flow: Seq[EventBookable]): Sequence = {
      eventBook
        .track(SEQUENCE_STARTED(name))
        .track(flow.map(_.toEventBook))
        .track(SEQUENCE_ENDED(name))
      this
    }
  }

  class SequenceFlow(name: String, val eventBook: EventBook, actorDoingSequence: FluentActor) extends EventBookable {

    def ->(call: => SequenceFlow): SequenceFlow = ???

    def and(): ActorContinueSequenceFlow = {
      new ActorContinueSequenceFlow(this, actorDoingSequence)
    }

    override def toEventBook: EventBook = eventBook

    class ActorContinueSequenceFlow(sequenceFlow: SequenceFlow, val subjectActor: FluentActor) extends Actorable {

      override def does(action: String): SequenceFlow = {
        sequenceFlow.eventBook.track(DONE(subjectActor, action))
        sequenceFlow
      }

      override def does(sequence: Sequence): SequenceFlow = {
        eventBook
          .track(NEW_SEQUENCE_SCHEDULED(subjectActor, sequence.name))
          .track(sequence.eventBook :: Nil)
        new SequenceFlow(s"$name ${sequence.name}", eventBook, subjectActor)
      }

      override def call(action: String, actor: FluentActor): SequenceFlow = {
        eventBook.track(CALLED(subjectActor, action, actor))
        new SequenceFlow(s"$name $action to ${actor.name}", eventBook, subjectActor)
      }

      override def reply(action: String, toActor: FluentActor): SequenceFlow = {
        eventBook.track(REPLIED(subjectActor, action, toActor))
        new SequenceFlow(s"$name replied $action to ${toActor.name}", eventBook, subjectActor)
      }

      override def check(condition: String): SequenceFlow = ???

      override def stop(): SequenceFlow = ???

      override def fire(action: String, toActor: FluentActor): SequenceFlow = {
        eventBook.track(FIRED(subjectActor, action, toActor))
        new SequenceFlow(s"$name $action to ${toActor.name}", eventBook, subjectActor)
      }

      override def launch(tracking: Sequence): SequenceFlow = ???

      override def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
    }

  }

  class FluentActor(name: String, val entity: ActorType = SEQUENCE_ACTOR_TYPE())
      extends Actor(entity, name)
      with Actorable {

    override def does(sequence: Sequence): SequenceFlow = {
      val eventBook = EventBook()
        .track(NEW_SEQUENCE_SCHEDULED(this, sequence.name))
        .track(sequence.eventBook :: Nil)
      new SequenceFlow(s"$name ${sequence.name}", eventBook, this)
    }

    override def does(action: String): SequenceFlow = {
      val eventBook = EventBook()
        .track(DONE(this, action))
      new SequenceFlow(s"$name $action", eventBook, this)
    }

    override def call(action: String, actor: FluentActor): SequenceFlow = {
      val eventBook = EventBook()
        .track(CALLED(this, action, actor))
      new SequenceFlow(s"$name $action to ${actor.name}", eventBook, this)
    }

    override def reply(action: String, toActor: FluentActor): SequenceFlow = {
      val eventBook = EventBook()
        .track(REPLIED(this, action, toActor))
      new SequenceFlow(s"$name replied $action to ${toActor.name}", eventBook, this)
    }

    override def stop(): SequenceFlow = ???

    override def check(condition: String): SequenceFlow = ???

    override def fire(action: String, toActor: FluentActor): SequenceFlow = {
      val eventBook = EventBook()
        .track(FIRED(this, action, toActor))
      new SequenceFlow(s"$name $action to ${toActor.name}", eventBook, this)
    }

    override def launch(tracking: Sequence): SequenceFlow = ???

    override def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow = ???
  }

  class User(role: String) extends FluentActor(name = role, entity = USER_TYPE()) {}

}
