package com.gnosly.fluentsequence.view.model.builder

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.ViewModels._

import scala.collection.mutable

class ViewModelComponentBuilder() {
  private val _actors: mutable.HashMap[String, ActorModelBuilder] = mutable.HashMap()
  private val _sequenceComponents: mutable.ListBuffer[SequenceModel] = mutable.ListBuffer[SequenceModel]()
  private val _alternatives: mutable.ListBuffer[AlternativeComponent] = mutable.ListBuffer[AlternativeComponent]()
  private var lastSignalIndex = -1

  def sequenceStarted(name: String): Unit = {
    _sequenceComponents += SequenceModel(name, lastSignalIndex)
  }

  def alternativeStarted(condition: String): Unit = {
    _alternatives += AlternativeComponent(_alternatives.size, condition, lastSignalIndex)
  }

  def alternativeEnded(condition: String): Unit = {
    _alternatives
      .filter(a => a.condition == condition)
      .head
      .end(lastSignalIndex)
  }

  def done(who: core.Actor, something: String): Unit = {
    lastSignalIndex += 1
    val actor = createOrGet(who)
    /*_signals += */
    actor.done(something, lastSignalIndex)
  }

  def called(who: core.Actor, something: String, toSomebody: core.Actor): SignalModel = {
    lastSignalIndex += 1
    val caller = createOrGet(who)
    val called = createOrGet(toSomebody)
    /*_signals += */
    caller.called(called, something, lastSignalIndex)
  }

  def replied(who: core.Actor, something: String, toSomebody: core.Actor): Unit = {
    lastSignalIndex += 1
    val replier = createOrGet(who)
    val replied = createOrGet(toSomebody)
    /*_signals += */
    replier.replied(replied, something, lastSignalIndex)
    replier.build(lastSignalIndex, -1) //TODO
  }

  def fired(who: core.Actor, something: String, toSomebody: core.Actor): SignalModel = {
    lastSignalIndex += 1
    val caller = createOrGet(who)
    val called = createOrGet(toSomebody)
    /*_signals += */
    caller.fired(called, something, lastSignalIndex)
  }

  private def createOrGet(who: core.Actor): ActorModelBuilder = {
    val actor = _actors.getOrElse(who.name, {
      val newActor = new ActorModelBuilder(_actors.size, who.name)
      _actors += who.name -> newActor
      newActor
    })

    actor
  }

  def build(): ViewModel = {
    val lastActor: ActorModelBuilder = _actors.values.maxBy(a => a.id)
    val actorModels = _actors.values.map(a => a.build(lastSignalIndex, lastActor.id)).toList

    ViewModel(
      actorModels,
      _sequenceComponents.toList,
      _alternatives.toList,
      lastSignalIndex
    )
  }
}
