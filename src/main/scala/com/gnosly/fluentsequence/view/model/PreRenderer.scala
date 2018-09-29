package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.component.SignalComponent

trait PreRenderer {
  def preRender(actor: ActorComponent): Box

  def preRender(activity: ActivityComponent): Box

  def preRender(signal: SignalComponent): Box
}
