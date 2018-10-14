package com.gnosly.fluentsequence.view.model.component
import com.gnosly.fluentsequence.view.model.ViewModels.ActivityModel
import com.gnosly.fluentsequence.view.model.ViewModels.ActorModel
import com.gnosly.fluentsequence.view.model.builder.ActorModelBuilder
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ActorModelBuilderTest extends FunSuite with Matchers {
  val ACTOR_ID = 0
  val ACTOR_NAME = "user"

  test("done") {
    val actorBuilder = new ActorModelBuilder(ACTOR_ID, ACTOR_NAME)
    actorBuilder.done("something", 0)
    actorBuilder.build(10, ACTOR_ID) shouldBe ActorModel(ACTOR_ID,
                                                         ACTOR_NAME,
                                                         true,
                                                         List(ActivityModel(0, ACTOR_ID, 0, 10)))

//    actor.activities.head shouldBe
//      new ActivityModelBuilder(0,
//                               0,
//                               0,
//                               0,
//                               true,
//                               mutable.ListBuffer[PointOnTheRight](
//                                 PointOnTheRight(0, new AutoSignalModel("something", 0, 0, 0)),
//                               ))
  }
}
