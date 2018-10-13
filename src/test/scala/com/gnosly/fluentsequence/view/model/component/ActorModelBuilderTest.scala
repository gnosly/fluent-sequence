package com.gnosly.fluentsequence.view.model.component
import com.gnosly.fluentsequence.view.model.ViewModels.AutoSignalModel
import com.gnosly.fluentsequence.view.model.ViewModels.PointOnTheRight
import com.gnosly.fluentsequence.view.model.builder.ActivityModelBuilder
import com.gnosly.fluentsequence.view.model.builder.ActorModelBuilder
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable

class ActorModelBuilderTest extends FunSuite with Matchers {

  test("done") {
    val actor = new ActorModelBuilder(0, "user")
    actor.done("something", 0)

    actor.activities.head shouldBe
      new ActivityModelBuilder(0,
                               0,
                               0,
                               0,
                               true,
                               mutable.ListBuffer[PointOnTheRight](
                                 PointOnTheRight(0, new AutoSignalModel("something", 0, 0, 0)),
                               ))
  }
}
