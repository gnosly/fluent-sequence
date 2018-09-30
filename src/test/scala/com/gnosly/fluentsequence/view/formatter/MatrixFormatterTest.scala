package com.gnosly.fluentsequence.view.formatter
import com.gnosly.fluentsequence.view.model.component.ActorComponent
import com.gnosly.fluentsequence.view.model.point.Fixed1DPoint
import org.scalatest.FunSuite
import org.scalatest.Matchers

class MatrixFormatterTest extends FunSuite with Matchers {
  val ACTOR_NAME = "user"

  test("column with only actor definition") {
    val formatter = new MatrixFormatter(new FixedPreRenderer)
    val actor = new ActorComponent(0, ACTOR_NAME)

    val matrixPoint = formatter.format(actor)

    matrixPoint shouldBe MatrixPoint(Fixed1DPoint((FixedPreRenderer.ACTOR_PADDING + ACTOR_NAME.length) / 2))
  }

}
