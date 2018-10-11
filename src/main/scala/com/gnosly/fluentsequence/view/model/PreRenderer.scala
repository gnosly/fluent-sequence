package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.component.SignalModel

trait PreRenderer {
  def preRender(actor: ActorModel): Box

  def preRender(actor: ActorComponent): Box

  def preRender(activity: ActivityModel): Box

  def preRender(signal: SignalModel): Box
}
