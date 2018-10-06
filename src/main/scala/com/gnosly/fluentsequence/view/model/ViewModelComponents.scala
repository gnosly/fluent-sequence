package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.component.SequenceComponent

import scala.collection.mutable

object ViewModelComponentsFactory {

  def viewModelFrom(book: EventBook) = {
    val viewModel = ViewModelComponents()
    val list = book.toTimelineEventList
    list.foreach(
      t => {
        t.event match {
          case DONE(who, something)                => viewModel.done(who, something)
          case CALLED(who, something, toSomebody)  => viewModel.called(who, something, toSomebody)
          case FIRED(who, something, toSomebody)   => viewModel.fired(who, something, toSomebody)
          case REPLIED(who, something, toSomebody) => viewModel.replied(who, something, toSomebody)
          case SEQUENCE_STARTED(name)              => viewModel.sequenceStarted(name)
          case other                               => println(s"WARN ignoring ${other} creating view model")
        }
      }
    )
    viewModel.end()
    viewModel
  }
}

case class ViewModelComponents(private val _actors: mutable.HashMap[String, ActorComponent] = mutable.HashMap(),
                               sequenceComponents: mutable.ListBuffer[SequenceComponent] =
                                 mutable.ListBuffer[SequenceComponent]()) {

  var lastSignalIndex = -1

  def sequenceStarted(name: String): Unit = {
    sequenceComponents += new SequenceComponent(name, lastSignalIndex)
  }

  def done(who: core.Actor, something: String): Unit = {
    lastSignalIndex += 1
    val actor = createOrGet(who)
    /*_signals += */
    actor.done(something, lastSignalIndex)
  }

  def called(who: core.Actor, something: String, toSomebody: core.Actor) = {
    lastSignalIndex += 1
    val caller = createOrGet(who)
    val called = createOrGet(toSomebody)
    /*_signals += */
    caller.called(called, something, lastSignalIndex)
  }

  private def createOrGet(who: core.Actor): ActorComponent = {
    val actor = _actors.getOrElse(who.name, {
      val newActor = new ActorComponent(_actors.size, who.name)
      _actors += who.name -> newActor
      newActor
    })

    actor
  }

  def replied(who: core.Actor, something: String, toSomebody: core.Actor) = {
    lastSignalIndex += 1
    val replier = createOrGet(who)
    val replied = createOrGet(toSomebody)
    /*_signals += */
    replier.replied(replied, something, lastSignalIndex)
    replier.end(lastSignalIndex)
  }

  def fired(who: core.Actor, something: String, toSomebody: core.Actor) = {
    lastSignalIndex += 1
    val caller = createOrGet(who)
    val called = createOrGet(toSomebody)
    /*_signals += */
    caller.fired(called, something, lastSignalIndex)
  }

  def lastActorId: Int = _actors.size - 1

  def end() = {
    _actors.foreach(a => a._2.end(lastSignalIndex))
    _actors.maxBy(a => a._2.id)._2.markAsLast
  }

  def actors(): Iterable[ActorComponent] = _actors.values

}
