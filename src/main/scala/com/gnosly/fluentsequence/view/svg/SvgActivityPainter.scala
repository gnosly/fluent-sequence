package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.Coordinates.{Activity, Actor}
import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.{ComponentPainter, Fixed2dPoint}

class SvgActivityPainter extends ComponentPainter[ActivityComponent]{
	override def paint(activity: ActivityComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
		val canvas = new SvgCanvas()

		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
		val bottomMiddleActor = pointMap(Actor.bottomMiddle(activity.actorId))
		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

		canvas.drawLine(bottomMiddleActor.up(1), bottomLeftActivity.down(1).right(1))
		canvas.drawRect(topLeftActivity, 2, bottomLeftActivity.y - topLeftActivity.y)
		canvas
	}
}
