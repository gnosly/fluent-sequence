package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.Coordinates.{Activity, Actor}
import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.svg.SvgCanvas
import com.gnosly.fluentsequence.view.{ComponentPainter, Fixed2dPoint}

class SvgActivityPainter extends ComponentPainter[ActivityComponent]{
	override def paint(activity: ActivityComponent, pointMap: Map[String, Fixed2dPoint]): SvgCanvas = {
		val canvas = new SvgCanvas()

		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

		if (activity.isFirst()) {
			val timelineStart = pointMap(Actor.bottomMiddle(activity.actorId)).up(1)
			canvas.drawLine(timelineStart, bottomLeftActivity.down(1).right(1))
		} else {
			val previousBottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id - 1))
			canvas.drawLine(previousBottomLeftActivity.down(1).right(1), bottomLeftActivity.down(1).right(1))
		}

		canvas.drawRect(topLeftActivity, 2, bottomLeftActivity.y - topLeftActivity.y)
		canvas

//		val existNextActivity = pointMap.contains(Activity.bottomLeft(activity.actorId, activity.id + 1))
//		if (!existNextActivity) { // not exist //todo
//			val bottomLeft = pointMap(Activity.bottomLeft(activity.actorId, activity.id))
//			canvas.write(bottomLeft.down(1).right(1), "|")
//		}
//
//		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
//		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))
//
//		canvas.write(topLeftActivity, "_|_")
//
//		val activityStart = topLeftActivity.down(1)
//		for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y) {
//			canvas.write(activityStart.down(i), "| |")
//		}
//
//		canvas.write(bottomLeftActivity, "|_|")
	}
}
