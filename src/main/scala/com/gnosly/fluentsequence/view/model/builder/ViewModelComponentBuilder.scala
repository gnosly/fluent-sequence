package com.gnosly.fluentsequence.view.model.builder

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.view.model.AlternativeBuilder
import com.gnosly.fluentsequence.view.model.ViewModels._

import scala.collection.mutable

class ViewModelComponentBuilder() {
  private val _actors: mutable.HashMap[String, ActorModelBuilder] = mutable.HashMap()
  private val _sequenceComponents: mutable.ListBuffer[SequenceModel] = mutable.ListBuffer[SequenceModel]()
  private val _alternatives: mutable.HashMap[String, AlternativeBuilder] = mutable.HashMap[String, AlternativeBuilder]()
  private var lastSignalIndex = -1

  def sequenceStarted(timelineIndex: Int, name: String): Unit = {
    lastSignalIndex = timelineIndex
    _sequenceComponents += SequenceModel(name, timelineIndex)
  }

  def alternativeStarted(timelineIndex: Int, condition: String): Unit = {
    lastSignalIndex = timelineIndex
    _alternatives += condition -> new AlternativeBuilder(_alternatives.size, condition, timelineIndex)
  }

  def alternativeEnded(timelineIndex: Int, condition: String): Unit = {
    lastSignalIndex = timelineIndex
    _alternatives(condition)
      .end(timelineIndex)
  }

  def done(timelineIndex: Int, who: core.Actor, something: String): Unit = {
    lastSignalIndex = timelineIndex
    val actor = createOrGet(who)
    /*_signals += */
    actor.done(something, timelineIndex)
  }

  def called(timelineIndex: Int, who: core.Actor, something: String, toSomebody: core.Actor): SignalModel = {
    lastSignalIndex = timelineIndex
    val caller = createOrGet(who)
    val called = createOrGet(toSomebody)
    /*_signals += */
    caller.called(called, something, timelineIndex)
  }

  def replied(timelineIndex: Int, who: core.Actor, something: String, toSomebody: core.Actor): Unit = {
    lastSignalIndex = timelineIndex
    val replier = createOrGet(who)
    val replied = createOrGet(toSomebody)
    /*_signals += */
    replier.replied(replied, something, timelineIndex)
    replier.build(timelineIndex, -1) //TODO
  }

  def fired(timelineIndex: Int, who: core.Actor, something: String, toSomebody: core.Actor): SignalModel = {
    lastSignalIndex = timelineIndex
    val caller = createOrGet(who)
    val called = createOrGet(toSomebody)
    /*_signals += */
    caller.fired(called, something, timelineIndex)
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
      _alternatives.values.map(_.build()).toList,
      lastSignalIndex
    )
  }
}
