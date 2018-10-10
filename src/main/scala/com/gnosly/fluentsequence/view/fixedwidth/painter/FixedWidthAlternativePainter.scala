package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.fixedwidth.FixedWidthCanvas
import com.gnosly.fluentsequence.view.formatter.PointableResolverAlgorithms.ResolvedPoints
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Canvas
import com.gnosly.fluentsequence.view.model.ComponentPainter
import com.gnosly.fluentsequence.view.model.Coordinates

class FixedWidthAlternativePainter extends ComponentPainter[AlternativeComponent] {
  override def paint(model: AlternativeComponent, pointMap: ResolvedPoints): FixedWidthCanvas = {
    val alternativeTopLeft = pointMap(Coordinates.Alternative.topLeft(model.id))
    val alternativeBottomRight = pointMap(Coordinates.Alternative.bottomRight(model.id))

    val canvas = new FixedWidthCanvas()
    val distance = alternativeBottomRight.x - alternativeTopLeft.x
    canvas
      .write(alternativeTopLeft, r("-", distance + 1))
      .write(alternativeTopLeft.down(1).right(2), model.condition + " /")
      .write(alternativeTopLeft.down(2), r("-", model.condition.length + 2) + "´")

    for (y <- 1L to alternativeBottomRight.y - alternativeTopLeft.y) {
      canvas.write(alternativeTopLeft.down(y), "|")
      canvas.write(alternativeTopLeft.down(y).right(alternativeBottomRight.x), "|")
    }

    canvas.write(alternativeTopLeft.right(1).atY(alternativeBottomRight.y), r("_", distance - 1))
  }

  //FIXME remove duplication code
  def r(pattern: String, count: Long): String =
    (0 until count.toInt).map(_ => pattern).reduce(_ + _)

}
