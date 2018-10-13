package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModels._
import com.gnosly.fluentsequence.view.model.builder.ViewModelComponentBuilder
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
}
