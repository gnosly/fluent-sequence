package com.gnosly.fluentsequence.view.svg.painter
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.ViewModels.AlternativeModel
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import com.gnosly.fluentsequence.view.svg.SvgCanvas

class SvgAlternativePainter extends ComponentPainter[AlternativeModel] {

  override def paint(model: AlternativeModel, pointMap: ResolvedPoints): SvgCanvas = {
    val topLeft = pointMap(Coordinates.Alternative.topLeft(model.id))
    val bottomRight = pointMap(Coordinates.Alternative.bottomRight(model.id))
    val width = bottomRight.x - topLeft.x
    val height = bottomRight.y - topLeft.y

    new SvgCanvas().drawBox(topLeft, width, height, model.condition)
  }
}
