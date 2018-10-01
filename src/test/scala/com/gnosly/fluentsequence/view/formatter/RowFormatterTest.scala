package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.AutoSignalComponent
import com.gnosly.fluentsequence.view.model.point.{Fixed1DPoint, ReferencePoint}
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RowFormatterTest extends FunSuite with Matchers {
  val SIGNAL_INDEX = 1

	test("autosignal row") {
		val formatter = new RowFormatter

		val point = formatter.format(new AutoSignalComponent("", SIGNAL_INDEX, 0, 0))

		point shouldBe RowPoint(SIGNAL_INDEX, new ReferencePoint(Activity.pointEnd(0, 0, SIGNAL_INDEX, "right")).y)
	}
}
