package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel

class FixedWidthActivityPainter() extends ComponentPainter[ActivityModel] {

  override def paint(activity: ActivityModel, pointMap: ResolvedPoints): FixedWidthCanvas = {
    val canvas = new FixedWidthCanvas

    if (activity.isFirst) {
      val timelineStart = pointMap(Actor.bottomMiddle(activity.actorId))
      val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
      0L until topLeftActivity.y - timelineStart.y foreach (i => canvas.write(timelineStart.down(i), "|"))
    } else {
      val previousBottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id - 1))
      val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))

      1L until topLeftActivity.y - previousBottomLeftActivity.y foreach (i =>
        canvas.write(previousBottomLeftActivity.down(i), "|"))
    }

    val existNextActivity = pointMap.contains(Activity.bottomLeft(activity.actorId, activity.id + 1))
    if (!existNextActivity) { // not exist //todo
      val bottomLeft = pointMap(Activity.bottomLeft(activity.actorId, activity.id))
      canvas.write(bottomLeft.down(1).right(1), "|")
    }

    val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
    val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

    canvas.write(topLeftActivity, "_|_")

    val activityStart = topLeftActivity.down(1)
    for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y) {
      canvas.write(activityStart.down(i), "| |")
    }

    canvas.write(bottomLeftActivity, "|_|")
  }

}
