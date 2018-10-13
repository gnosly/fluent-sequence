package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.Coordinates.Actor
import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgActivityPainter extends ComponentPainter[ActivityModel] {
  override def paint(activity: ActivityModel, pointMap: ResolvedPoints): SvgCanvas = {
    val canvas = new SvgCanvas

    val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
    val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

    if (activity.isFirst) {
      val timelineStart = pointMap(Actor.bottomMiddle(activity.actorId)).up(1)
      canvas.drawLine(timelineStart, bottomLeftActivity.down(1).right(1))
    } else {
      val previousBottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id - 1))
      canvas.drawLine(previousBottomLeftActivity.down(1).right(1), bottomLeftActivity.down(1).right(1))
    }

    canvas.drawRect(topLeftActivity, 2, bottomLeftActivity.y - topLeftActivity.y)
  }
}
