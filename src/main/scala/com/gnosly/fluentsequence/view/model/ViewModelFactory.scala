package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.core._
import com.gnosly.fluentsequence.view.model.ViewModels._
import com.gnosly.fluentsequence.view.model.builder.ViewModelBuilder

object ViewModelFactory {

  def viewModelFrom(book: EventBook): ViewModel = {
    val viewModel = new ViewModelBuilder()
    val list = book.toTimelineEventList
    list.foreach(
      t => {
        t.event match {
          case DONE(who, something)                => viewModel.done(t.index, who, something)
          case CALLED(who, something, toSomebody)  => viewModel.called(t.index, who, something, toSomebody)
          case FIRED(who, something, toSomebody)   => viewModel.fired(t.index, who, something, toSomebody)
          case REPLIED(who, something, toSomebody) => viewModel.replied(t.index, who, something, toSomebody)
          case SEQUENCE_STARTED(name)              => viewModel.sequenceStarted(t.index, name)
          case ALTERNATIVE_STARTED(condition)      => viewModel.alternativeStarted(t.index, condition)
          case ALTERNATIVE_ENDED(condition)        => viewModel.alternativeEnded(t.index, condition)
          case other                               => println(s"WARN ignoring $other creating view model")
        }
      }
    )
    viewModel.build()
  }
}
