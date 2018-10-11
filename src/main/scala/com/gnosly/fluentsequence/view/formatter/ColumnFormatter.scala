package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTIVITY_FIXED_WIDTH
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.formatter.point.ColumnPoint
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.component._

class ColumnFormatter(fixedPreRenderer: FixedPreRenderer) {

  def format(actor: ActorComponent): Pointable = {
    val actorWidth = fixedPreRenderer.preRender(actor).width
    val minWidth = columnWidthForcedBy(actor, actorWidth)

    val result = actor.activities
      .flatMap(a => a.rightPoints)
      .foldLeft(minWidth)((acc, e) => {
        acc max columnWidthForcedBySignal(e.signalComponent, actorWidth)
      })

    ColumnPoint(actor.id, result)
  }

  private def columnWidthForcedBy(actor: ActorComponent, actorWidth: Long) = {
    if (actor.isLast) {
      actorWidth
    } else {
      actorWidth + DISTANCE_BETWEEN_ACTORS
    }
  }

  private def columnWidthForcedBySignal(signal: SignalModel, actorWidth: Long) = {
    val signalWidth = fixedPreRenderer.preRender(signal).width
    signalWidth + (actorWidth + ACTIVITY_FIXED_WIDTH) / 2
  }
}
