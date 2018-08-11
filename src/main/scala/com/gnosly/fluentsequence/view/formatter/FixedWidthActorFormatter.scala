package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.Coordinates.{Actor, Pointable, ViewMatrix}
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.{DISTANCE_BETWEEN_ACTORS, LEFT_MARGIN, TOP_MARGIN}
import com.gnosly.fluentsequence.view.formatter.point.ActorPoints
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.PointMath.max
import com.gnosly.fluentsequence.view.model.point._

class FixedWidthActorFormatter(preRenderer: PreRenderer) {
	def format(actor: ActorComponent): Pointable = {
		def previousActorDistanceOrDefault(): Point2d = {
			if (actor.id == 0)
				return new Variable2DPoint(LEFT_MARGIN, TOP_MARGIN)
			else {
				return new ReferencePoint(Actor.topLeft(actor.id - 1))
					.right(max(Reference1DPoint(ViewMatrix.column(actor.id - 1)), Fixed1DPoint(DISTANCE_BETWEEN_ACTORS)))
			}
		}

		//1. prerenderizzazione
		val actorBox = preRenderer.preRender(actor)
		//2. determinazione punto in alto a sx
		val actorTopLeft = previousActorDistanceOrDefault()
		new ActorPoints(actor.id, actorTopLeft, actorBox)
	}
}