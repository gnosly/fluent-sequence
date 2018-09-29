package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.model.Box
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.component._

class FixedPreRenderer extends PreRenderer {
  override def preRender(actorComponent: ActorComponent): Box = Box(s"| ${actorComponent.name} |".length, 4)

  override def preRender(activity: ActivityComponent): Box = Box(2, 2)

  override def preRender(signalComponent: SignalComponent) = signalComponent match {
    case x: AutoSignalComponent => Box(x.name.length + 6, 4)
    case x: BiSignalComponent   => Box(x.name.length + 5, 2)
  }
}
