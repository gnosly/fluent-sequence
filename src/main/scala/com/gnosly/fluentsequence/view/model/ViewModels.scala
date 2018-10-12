package com.gnosly.fluentsequence.view.model
import com.gnosly.fluentsequence.view.model.component.PointModel
import com.gnosly.fluentsequence.view.model.component.PointOnTheRight
import com.gnosly.fluentsequence.view.model.component.SequenceModel

object ViewModels {

  case class ViewModel(actors: List[ActorModel],
                       activities: List[ActivityModel],
                       points: List[PointModel],
                       sequenceComponents: List[SequenceModel],
                       alternatives: List[AlternativeComponent],
                       lastSignalIndex: Int) {
    def rightPoints: List[PointModel] = points.filter { _.isInstanceOf[PointOnTheRight] }

    def lastActorId: Int = actors.size - 1
  }

  case class ActorModel(id: Int, name: String, isLast: Boolean)
  case class ActivityModel(id: Int, actorId: Int, fromIndex: Int, toIndex: Int) {
    def isFirst: Boolean = id == 0
  }
}
