package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.point.PointMap
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

object PointableResolverAlgorithms {

  val loopPointableResolverAlgorithm = new LoopPointableResolverAlgorithm

  trait PointableResolverAlgorithm {
    def resolve(pointables: Seq[Pointable]): ResolvedPoints
  }

  class LoopPointableResolverAlgorithm extends PointableResolverAlgorithm {
    override def resolve(pointables: Seq[Pointable]): ResolvedPoints = {

      val pointMap = new PointMap

      do () while ({
        val previousPointMap = pointMap.toMap

        pointMap.putAll(pointables.flatMap(p => p.toPoints(pointMap.toMap)))

        pointMap.toMap != previousPointMap
      })

      pointMap.toMap
    }

  }

}
