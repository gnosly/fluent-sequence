package com.gnosly.fluentsequence.view.model

import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.ViewModels.SignalModel

trait PreRenderer {
  def preRender(actor: ActorModel): Box

  def preRender(activity: ActivityModel): Box

  def preRender(signal: SignalModel): Box
}
