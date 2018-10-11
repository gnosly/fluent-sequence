package com.gnosly.fluentsequence.view.model.component

case class AutoSignalComponent(name: String, index: Int, actorId: Int, activityId: Int) extends SignalComponent {

  override def currentIndex: Int = index
}
