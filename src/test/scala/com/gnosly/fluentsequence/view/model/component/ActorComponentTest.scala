package com.gnosly.fluentsequence.view.model.component
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable

class ActorComponentTest extends FunSuite with Matchers {

  test("done") {
    val actor = new ActorComponent(0, "user")
    actor.done("something", 0)

    actor.activities.head shouldBe
      new ActivityComponent(0,
                            0,
                            0,
                            0,
                            true,
                            mutable.ListBuffer[PointOnTheRight](
                              PointOnTheRight(0, new AutoSignalModel("something", 0, 0, 0)),
                            ))
  }
}
