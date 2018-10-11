package com.gnosly.fluentsequence.view.model

object ViewModels {
  case class ActorModel(id: Int, name: String)
  case class ActivityModel(id: Int, actorId: Int, fromIndex: Int, toIndex: Int) {
    def isFirst: Boolean = id == 0
  }
}
