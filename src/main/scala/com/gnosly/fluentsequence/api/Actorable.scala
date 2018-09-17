package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.api.FluentSequence.{FluentActor, Sequence, SequenceFlow}

trait Actorable {

  def check(condition: String): SequenceFlow

  def stop(): SequenceFlow

  def fire(event: String): SequenceFlow

  def launch(tracking: Sequence): SequenceFlow

  def does(sequence: Sequence): SequenceFlow

  def does(action: String): SequenceFlow

  def call(action: String, actor: FluentActor): SequenceFlow

  def reply(action: String, actor: FluentActor): SequenceFlow

  def forEach(item: String, sequenceFlow: SequenceFlow): SequenceFlow
}
