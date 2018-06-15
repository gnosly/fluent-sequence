package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates.{Actor, Pointable, ViewMatrix}
import com.gnosly.fluentsequence.view.fixedwidth.FormatterConstants.{DISTANCE_BETWEEN_ACTORS, LEFT_MARGIN, TOP_MARGIN}
import com.gnosly.fluentsequence.view.fixedwidth.PointMath.max
import com.gnosly.fluentsequence.view.model.ActorComponent
import com.gnosly.fluentsequence.view.model.point.ActorPoints

class FixedWidthActorFormatter(painter: FixedWidthPainter) {
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
		val actorBox = painter.preRender(actor)
		//2. determinazione punto in alto a sx
		val actorTopLeft = previousActorDistanceOrDefault()
		new ActorPoints(actor.id, actorTopLeft, actorBox)
	}
}