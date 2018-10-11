package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core
import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.component._

import scala.collection.mutable

case class ViewModel(actorsM: List[ActorModel],
                     activities: List[ActivityModel],
                     points: List[PointModel],
                     actors: List[ActorComponent],
                     sequenceComponents: List[SequenceModel],
                     alternatives: List[AlternativeComponent],
                     lastSignalIndex: Int) {
  def rightPoints: List[PointModel] = points.filter { _.isInstanceOf[PointOnTheRight] }

  def firstActor(): ActorComponent = actors.head
  def lastActorId: Int = actors.size - 1
}

object ViewModelComponentsFactory {

  def viewModelFrom(book: EventBook): ViewModel = {
    val viewModel = new ViewModelComponentBuilder()
    val list = book.toTimelineEventList
    list.foreach(
      t => {
        t.event match {
          case DONE(who, something)                => viewModel.done(who, something)
          case CALLED(who, something, toSomebody)  => viewModel.called(who, something, toSomebody)
          case FIRED(who, something, toSomebody)   => viewModel.fired(who, something, toSomebody)
          case REPLIED(who, something, toSomebody) => viewModel.replied(who, something, toSomebody)
          case SEQUENCE_STARTED(name)              => viewModel.sequenceStarted(name)
          case ALTERNATIVE_STARTED(condition)      => viewModel.alternativeStarted(condition)
          case ALTERNATIVE_ENDED(condition)        => viewModel.alternativeEnded(condition)
          case other                               => println(s"WARN ignoring $other creating view model")
        }
      }
    )
    viewModel.build()
  }

  private class ViewModelComponentBuilder(
      private val _actors: mutable.HashMap[String, ActorComponent] = mutable.HashMap(),
      private val _sequenceComponents: mutable.ListBuffer[SequenceModel] = mutable.ListBuffer[SequenceModel](),
      private val _alternatives: mutable.ListBuffer[AlternativeComponent] = mutable.ListBuffer[AlternativeComponent]()) {
    private var lastSignalIndex = -1

    def sequenceStarted(name: String): Unit = {
      _sequenceComponents += new SequenceModel(name, lastSignalIndex)
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

    private def createOrGet(who: core.Actor): ActorComponent = {
      val actor = _actors.getOrElse(who.name, {
        val newActor = new ActorComponent(_actors.size, who.name)
        _actors += who.name -> newActor
        newActor
      })

      actor
    }

    def replied(who: core.Actor, something: String, toSomebody: core.Actor): Unit = {
      lastSignalIndex += 1
      val replier = createOrGet(who)
      val replied = createOrGet(toSomebody)
      /*_signals += */
      replier.replied(replied, something, lastSignalIndex)
      replier.end(lastSignalIndex)
    }

    def fired(who: core.Actor, something: String, toSomebody: core.Actor): SignalModel = {
      lastSignalIndex += 1
      val caller = createOrGet(who)
      val called = createOrGet(toSomebody)
      /*_signals += */
      caller.fired(called, something, lastSignalIndex)
    }

    def build(): ViewModel = {
      _actors.foreach(a => a._2.end(lastSignalIndex))
      _actors.maxBy(a => a._2.id)._2.markAsLast

      ViewModel(
        _actors.values.map(a => ActorModel(a.id, a.name)).toList,
        _actors.values
          .flatMap(a => a.activities)
          .map(a => ActivityModel(a.id, a.actorId, a.fromIndex, a.toIndex))
          .toList,
        _actors.values.flatMap(_.activities).flatMap(_.points).toList,
        _actors.values.toList,
        _sequenceComponents.toList,
        _alternatives.toList,
        lastSignalIndex
      )
    }
  }

}
