package com.gnosly.fluentsequence.view.model

object ViewModels {

  trait PointModel {
    def signalComponent: SignalModel
  }

  trait SignalModel {
    def currentIndex: Int
    def fromActorIdd: Int
  }

  case class ViewModel(actors: List[ActorModel],
                       sequenceComponents: List[SequenceModel],
                       alternatives: List[AlternativeComponent],
                       lastSignalIndex: Int) {
    def points: List[PointModel] = actors.flatMap(_.activities.flatMap(a => a.leftPoints ++ a.rightPoints))

    def rightPoints: List[PointOnTheRight] = actors.flatMap(_.activities.flatMap(_.rightPoints))

    def lastActorId: Int = actors.size - 1

    def activities: List[ActivityModel] = actors.flatMap(_.activities)
  }

  case class ActorModel(id: Int, name: String, isLast: Boolean, activities: List[ActivityModel] = List())

  case class ActivityModel(id: Int,
                           actorId: Int,
                           fromIndex: Int,
                           toIndex: Int,
                           rightPoints: List[PointOnTheRight] = List(),
                           leftPoints: List[PointOnTheLeft] = List()) {
    def isFirst: Boolean = id == 0
  }

  case class PointOnTheRight(id: Int, signal: SignalModel) extends PointModel {
    override def signalComponent: SignalModel = signal
  }

  case class PointOnTheLeft(id: Int, signal: BiSignalModel) extends PointModel {
    override def signalComponent: SignalModel = signal
  }

  case class BiSignalModel(name: String,
                           index: Int,
                           fromActorId: Int,
                           fromActivityId: Int,
                           toActorId: Int,
                           toActivityId: Int)
      extends SignalModel {

    def leftToRight: Boolean = fromActorId < toActorId
    override def currentIndex: Int = index
    override def fromActorIdd: Int = fromActorId
  }

  class SyncRequest(name: String,
                    override val index: Int,
                    override val fromActorId: Int,
                    override val fromActivityId: Int,
                    override val toActorId: Int,
                    override val toActivityId: Int)
      extends BiSignalModel(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

  class SyncResponse(name: String,
                     override val index: Int,
                     override val fromActorId: Int,
                     override val fromActivityId: Int,
                     override val toActorId: Int,
                     override val toActivityId: Int)
      extends BiSignalModel(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

  class AsyncRequest(name: String,
                     override val index: Int,
                     override val fromActorId: Int,
                     override val fromActivityId: Int,
                     override val toActorId: Int,
                     override val toActivityId: Int)
      extends BiSignalModel(name, index, fromActorId, fromActivityId, toActorId: Int, toActivityId)

  case class AutoSignalModel(name: String, index: Int, actorId: Int, activityId: Int) extends SignalModel {

    override def currentIndex: Int = index
    override def fromActorIdd: Int = actorId
  }

  case class SequenceModel(name: String, startIndex: Int)
}
