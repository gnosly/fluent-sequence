package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.ViewModels._
import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.PreRenderer

class FixedPreRenderer extends PreRenderer {
  import FixedPreRenderer._

  override def preRender(actor: ActorModel): Box = Box(actor.name.length + ACTOR_PADDING, ACTOR_MIN_HEIGHT)

  override def preRender(activity: ActivityModel): Box = Box(ACTIVITY_FIXED_WIDTH, ACTIVITY_MIN_HEIGHT)

  override def preRender(signalComponent: SignalModel): Box = signalComponent match {
    case x: AutoSignalModel => Box(x.name.length + AUTO_SIGNAL_FIXED_PADDING, AUTO_SIGNAL_MIN_HEIGHT)
    case x: BiSignalModel   => Box(x.name.length + BISIGNAL_FIXED_PADDING, BISIGNAL_MIN_HEIGHT)
  }
}

object FixedPreRenderer {
  val ACTOR_PADDING = 4
  val ACTOR_MIN_HEIGHT = 4
  val ACTIVITY_FIXED_WIDTH = 2
  val ACTIVITY_MIN_HEIGHT = 2
  val AUTO_SIGNAL_MIN_HEIGHT = 4
  val BISIGNAL_MIN_HEIGHT = 2
  val AUTO_SIGNAL_FIXED_PADDING = 6
  val BISIGNAL_FIXED_PADDING = 5
}
