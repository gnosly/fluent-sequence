package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.LEFT_MARGIN
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.TOP_MARGIN
import com.gnosly.fluentsequence.view.formatter.point.ActorPoints
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.point._

class ActorFormatter(preRenderer: PreRenderer) {
  def format(actor: ActorModel): Pointable = {
    def previousActorDistanceOrDefault: Point2d = {
      if (actor.id == 0)
        new Variable2DPoint(LEFT_MARGIN, TOP_MARGIN) //FIXME margin should be inside WidthAndHeightFormatter
      else {
        new ReferencePoint(Actor.topLeft(actor.id - 1))
          .right(Reference1DPoint(ViewMatrix.column(actor.id - 1)) max Fixed1DPoint(DISTANCE_BETWEEN_ACTORS))
      }
    }

    //1. prerenderizzazione
    val actorBox = preRenderer.preRender(actor)
    //2. determinazione punto in alto a sx
    val actorTopLeft = previousActorDistanceOrDefault
    ActorPoints(actor.id, actorTopLeft, actorBox)
  }
}
