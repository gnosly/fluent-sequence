package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_MIN_HEIGHT
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.TOP_MARGIN
import com.gnosly.fluentsequence.view.model.Coordinates.Pointable
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.SignalModel
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints

class RowsFormatter(val formatter: RowFormatter = new RowFormatter()) {

  def format(signal: List[SignalModel]): List[Pointable] = {
    signal.map(formatter.format) :+ new Pointable {
      override def toPoints(pointMap: ResolvedPoints): Seq[(String, Fixed2dPoint)] = List(
        ViewMatrix.row(-1) -> Fixed2dPoint(TOP_MARGIN + ACTOR_MIN_HEIGHT, 0)
      )
    }
  }
}
