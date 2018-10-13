package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.model.AlternativeComponent
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FixedWidthAlternativePainterTest extends FunSuite with Matchers {
  val ALTERNATIVE_ID = 0

  test("render") {
    val painter = new FixedWidthAlternativePainter()

    val canvas =
      painter.paint(
        AlternativeComponent(ALTERNATIVE_ID, "condition", 0, 1),
        new ResolvedPoints(
          Map(
            Coordinates.Alternative.topLeft(ALTERNATIVE_ID) -> Fixed2dPoint(0, 0),
            Coordinates.Alternative.bottomRight(ALTERNATIVE_ID) -> Fixed2dPoint(28, 4)
          ))
      )

    println(canvas)

    canvas.print shouldBe
      /*  */ "-----------------------------" + "\n" +
        /**/ "| condition /               |" + "\n" +
        /**/ "|----------´                |" + "\n" +
        /**/ "|                           |" + "\n" +
        /**/ "|___________________________|"
  }
}
