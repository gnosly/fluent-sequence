package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.component._

class FixedPreRenderer extends PreRenderer {
  import FixedPreRenderer._

  override def preRender(actorComponent: ActorComponent): Box = {
    Box(actorComponent.name.length + ACTOR_PADDING, ACTOR_MIN_HEIGHT)
  }

  override def preRender(activity: ActivityComponent): Box = Box(ACTIVITY_FIXED_WIDTH, ACTIVITY_MIN_HEIGHT)

  override def preRender(signalComponent: SignalComponent) = signalComponent match {
    case x: AutoSignalComponent => Box(x.name.length + AUTO_SIGNAL_FIXED_PADDING, AUTO_SIGNAL_MIN_HEIGHT)
    case x: BiSignalComponent   => Box(x.name.length + BISIGNAL_FIXED_PADDING, BISIGNAL_MIN_HEIGHT)
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
