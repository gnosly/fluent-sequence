package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.formatter.FixedPreRenderer.ACTOR_PADDING
import com.gnosly.fluentsequence.view.formatter.FormatterConstants.DISTANCE_BETWEEN_ACTORS
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class MatrixFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"

  test("column with only one actor definition") {
    val formatter = new MatrixFormatter(new FixedPreRenderer)
    val actor = new ActorComponent(0, ACTOR_NAME, isLast = true)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint((ACTOR_PADDING + ACTOR_NAME.length) / 2))
  }

  test("column with actor that is not the last") {
    val formatter = new MatrixFormatter(new FixedPreRenderer)
    val actor = new ActorComponent(0, ACTOR_NAME, isLast = false)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint(ACTOR_PADDING + ACTOR_NAME.length + DISTANCE_BETWEEN_ACTORS))
  }

}
