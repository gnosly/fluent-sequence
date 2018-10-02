package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.Coordinates.Activity
import com.gnosly.fluentsequence.view.model.component.{AutoSignalComponent, SyncRequest, SyncResponse}
import com.gnosly.fluentsequence.view.model.point.ReferencePoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RowFormatterTest extends FunSuite with Matchers {
  val SIGNAL_INDEX = 1
  val formatter = new RowFormatter

  test("autosignal row") {

    val point = formatter.format(new AutoSignalComponent("", SIGNAL_INDEX, 0, 0))

    point shouldBe RowPoint(SIGNAL_INDEX, new ReferencePoint(Activity.pointEnd(0, 0, SIGNAL_INDEX, "right")).y)
  }

  test("signal right") {

    val point = formatter.format(new SyncRequest("", SIGNAL_INDEX, 0, 0, 1, 1))

    point shouldBe RowPoint(SIGNAL_INDEX, new ReferencePoint(Activity.pointEnd(0, 0, SIGNAL_INDEX, "right")).y)
  }

  test("signal left") {

		val point = formatter.format(new SyncResponse("", SIGNAL_INDEX, 1, 1, 0, 0))

    point shouldBe RowPoint(SIGNAL_INDEX, new ReferencePoint(Activity.pointEnd(1, 1, SIGNAL_INDEX, "left")).y)
  }
}
