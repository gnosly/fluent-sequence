package com.gnosly.fluentsequence.view.formatter.point
import com.gnosly.fluentsequence.view.model.Coordinates.Alternative
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import com.gnosly.fluentsequence.view.model.point.Fixed2dPoint
import com.gnosly.fluentsequence.view.model.point.Variable2DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class AlternativePointsTest extends FunSuite with Matchers {
  val ALTERNATIVE_ID = 0

  test("resolve") {
    AlternativePoints(ALTERNATIVE_ID,
                      Variable2DPoint(Fixed1DPoint(0), Fixed1DPoint(0)),
                      Variable2DPoint(Fixed1DPoint(1), Fixed1DPoint(1)))
      .toPoints(null) shouldBe Alternative.topLeft(ALTERNATIVE_ID) -> Fixed2dPoint(0, 0) ::
      Alternative.bottomRight(ALTERNATIVE_ID) -> Fixed2dPoint(1, 1) ::
      Nil
  }
}
