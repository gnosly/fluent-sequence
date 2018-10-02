package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.PointMap

object PointableResolverAlgorithms {
  val loopPointableResolverAlgorithm = new LoopPointableResolverAlgorithm

  trait PointableResolverAlgorithm {
    def resolve(pointables: Seq[Pointable]): Map[String, Fixed2dPoint]
  }

  class LoopPointableResolverAlgorithm extends PointableResolverAlgorithm {
    override def resolve(pointables: Seq[Pointable]): Map[String, Fixed2dPoint] = {

      val pointMap = new PointMap

      do () while ({
        val previousPointMap = pointMap.toMap

        pointMap.putAll(pointables.flatMap(p => p.toPoints(pointMap)))

        pointMap.toMap != previousPointMap
      })

      pointMap.toMap
    }

  }

}
