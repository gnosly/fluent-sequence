package com.gnosly.fluentsequence.view.svg.painter
import com.gnosly.fluentsequence.view.model.Coordinates.Alternative
import com.gnosly.fluentsequence.view.model.Coordinates.ViewMatrix
import com.gnosly.fluentsequence.view.model.ViewModels.AlternativeModel
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.ResolvedPoints
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SvgAlternativePainterTest extends FunSuite with Matchers {
  test("painting") {
    val ALTERNATIVE_START_INDEX = 0
    val ALTERNATIVE_END_INDEX = 3
    val ID = 0
    val NOT_IMPORTANT = 0

    val painter = new SvgAlternativePainter

    val canvas = painter.paint(
      AlternativeModel(ID, "condition", ALTERNATIVE_START_INDEX, ALTERNATIVE_END_INDEX),
      ResolvedPoints(
        Map(
          Alternative.topLeft(ID) -> Fixed2dPoint(10, 10),
          ViewMatrix.row(ALTERNATIVE_START_INDEX) -> Fixed2dPoint(15, NOT_IMPORTANT),
          ViewMatrix.row(ALTERNATIVE_END_INDEX) -> Fixed2dPoint(35, NOT_IMPORTANT),
          Alternative.bottomRight(ID) -> Fixed2dPoint(38, 20),
        ))
    )

    println(canvas.content)
    canvas.content shouldBe """<rect x="100" y="100" width="280" height="100" style="stroke-width: 2.0;stroke: black;fill: transparent" />
															|<rect x="101" y="101" width="71" height="29" style="stroke-width: 0;stroke: black;fill: white" />
															|<text x="110" y="120" font-size="16px" text-anchor="start">condition</text>
															|<line x1="100" y1="130" x2="172" y2="130" style="stroke:black;stroke-width:1.5;" />
															|<line x1="172" y1="130" x2="192" y2="100" style="stroke:black;stroke-width:1.5;" />""".stripMargin
  }
}
