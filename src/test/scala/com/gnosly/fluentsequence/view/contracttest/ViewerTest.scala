package com.gnosly.fluentsequence.view.contracttest
import com.gnosly.fluentsequence.api.FluentSequence.FluentActor
import com.gnosly.fluentsequence.api.FluentSequence.Sequence
import com.gnosly.fluentsequence.api.FluentSequence.User
import com.gnosly.fluentsequence.api.FluentSequence.inCase
import com.gnosly.fluentsequence.view.Viewer
import com.gnosly.fluentsequence.view.model.Canvas
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.io.Source

abstract class ViewerTest(viewer: Viewer) extends FunSuite with Matchers {
  val USER = new User("user")
  val SYSTEM = new FluentActor("system")

  test("multi actor") {
    val secondSystem = new FluentActor("another system")
    val thirdSystem = new FluentActor("third system")

    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.call("call2", secondSystem) ::
        secondSystem.call("call3", thirdSystem) ::
        thirdSystem.reply("reply3", secondSystem) ::
        secondSystem.reply("reply2", SYSTEM) ::
        SYSTEM.reply("reply", USER) ::
        USER.call("call4", thirdSystem) ::
        thirdSystem.call("call5", SYSTEM) ::
        SYSTEM.reply("reply4", thirdSystem) ::
        Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, multiActorExpected())
  }

  test("view an actor that calls another") {
    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) :: Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, viewAnActorThatCallsAnotherExpected())
  }

  test("do a two actor sequence") {
    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) :: Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, doATwoActorSequenceExpected())
  }

  test("do a complete sequence") {

    val flow = Sequence("example").startWith(
      USER.does("something") ::
        USER.does("something else") ::
        USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) :: Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, doACompleteSequenceExpected())
  }

  test("multi activity") {

    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) ::
        USER.call("finalize", SYSTEM) ::
        SYSTEM.reply("finalize done", USER) ::
        inCase("condition A", USER.does("something") :: Nil) ::
        inCase("condition B", USER.does("something else") :: Nil) ::
        Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, multiActivityExpected())
  }

  def viewAnActorThatCallsAnotherExpected(): String
  def doATwoActorSequenceExpected(): String
  def doACompleteSequenceExpected(): String
  def multiActivityExpected(): String
  def multiActorExpected(): String

  private def shouldBeLike(canvas: Canvas, sequence: String): Any = {
    println(canvas)
    canvas.print().replaceAll("\n", "") shouldBe sequence.replaceAll("\n\\s*", "")
  }

  protected def sequenceFromFile(filename: String) = {
    Source.fromResource(s"$filename").mkString
  }
}
