package com.gnosly.fluentsequence.view.formatter

import com.gnosly.fluentsequence.view.formatter.point.ActivityPoints
import com.gnosly.fluentsequence.view.model.Coordinates._
import com.gnosly.fluentsequence.view.model.PreRenderer
import com.gnosly.fluentsequence.view.model.component.ActivityComponent
import com.gnosly.fluentsequence.view.model.point.Reference1DPoint
import com.gnosly.fluentsequence.view.model.point.ReferencePoint

class ActivityFormatter(preRenderer: PreRenderer) {

  def format(activity: ActivityComponent): Pointable = {
    val actorBottomMiddle = new ReferencePoint(Actor.bottomMiddle(activity.actorId))
    //1. prerenderizzazione
    val activityBox = preRenderer.preRender(activity)
    //2. determinazione punto in alto a sx

    val activityStartY = {
      if (isFirstSignal(activity.fromIndex)) {
        actorBottomMiddle.y
      } else {
        val lastSignalEnd = Reference1DPoint(ViewMatrix.row(activity.fromIndex - 1))
        val marginSinceLastActivity =
          new ReferencePoint(Activity.bottomLeft(activity.actorId, activity.id - 1)).down(1).y

        lastSignalEnd.max(marginSinceLastActivity)
      }
    }

    val activityTopLeft = actorBottomMiddle.left(activityBox.halfWidth).atY(activityStartY)
    val activityEndY = Reference1DPoint(ViewMatrix.row(activity.toIndex))

    ActivityPoints(activity.actorId, activity.id, activityTopLeft, activityBox.width, activityEndY)
  }

  private def isFirstSignal(fromIndex: Int) = {
    fromIndex == 0
  }
}
