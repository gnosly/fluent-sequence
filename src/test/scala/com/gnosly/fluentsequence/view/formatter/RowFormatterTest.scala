package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RowFormatterTest extends FunSuite with Matchers {
  val SIGNAL_INDEX = 0

  test("first row") {
    val formatter = new RowFormatter(new FixedPreRenderer)

    val point = formatter.format(new AutoSignalComponent("name", SIGNAL_INDEX, 0, 0))

    point shouldBe RowPoint(SIGNAL_INDEX, Fixed1DPoint(FixedPreRenderer.AUTO_SIGNAL_MIN_HEIGHT))
  }
}
