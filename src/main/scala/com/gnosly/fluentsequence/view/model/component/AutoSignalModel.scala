package com.gnosly.fluentsequence.view.model.component

case class AutoSignalModel(name: String, index: Int, actorId: Int, activityId: Int) extends SignalModel {

  override def currentIndex: Int = index
}
