package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.fixedwidth.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.PointMath.max
import com.gnosly.fluentsequence.view.model.ActivityComponent
import com.gnosly.fluentsequence.view.model.point.ActivityPoints

class FixedWidthActivityFormatter(painter: FixedWidthPainter) {

	def format(activity: ActivityComponent): Pointable = {
		val actorBottomMiddle = new ReferencePoint(Actor.bottomMiddle(activity.actorId))
		//1. prerenderizzazione
		val activityBox = painter.preRender(activity)
		//2. determinazione punto in alto a sx

		val activityStartY = {
			if (activity.fromIndex > 1) {
				val lastSignalEnd = Reference1DPoint(ViewMatrix.row(activity.fromIndex - 1))
				val marginSinceLastActivity = new ReferencePoint(Activity.bottomLeft(activity.actorId, activity.id - 1)).down(1).y()
				max(lastSignalEnd, marginSinceLastActivity)
			} else {
				actorBottomMiddle.y()
			}
		}

		val activityTopLeft = actorBottomMiddle.left(activityBox.halfWidth()).atY(activityStartY)
		val activityEndY = Reference1DPoint(ViewMatrix.row(activity.toIndex))

		ActivityPoints(activity.actorId, activity.id, activityTopLeft, activityBox.width, activityEndY)
	}
}