package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.{ComponentPainter, Fixed2dPoint}

class SvgActivityPainter extends ComponentPainter[ActivityComponent]{
	override def paint(activity: ActivityComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
		val canvas = new SvgCanvas()

		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

		canvas.drawRect(topLeftActivity, 2, bottomLeftActivity.y - topLeftActivity.y)
		canvas
	}
}
