package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.api.FluentSequence.FluentActor
import com.gnosly.fluentsequence.api.FluentSequence.Sequence
import com.gnosly.fluentsequence.api.FluentSequence.User
import com.gnosly.fluentsequence.view.model.Canvas
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.io.Source

class SvgViewerTest extends FunSuite with Matchers {
  val USER = new User("user")
  val SYSTEM = new FluentActor("system")
  val viewer = new SvgViewer

  test("view an actor that calls another") {
    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) :: Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, sequenceFromFile("svg/two-actors-one-call.svg"))
  }

  test("do a two actor sequence") {
    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) :: Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, sequenceFromFile("svg/two-actors.svg"))
  }

  test("do a complete sequence") {

    val flow = Sequence("example").startWith(
      USER.does("something") ::
        USER.does("something else") ::
        USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) :: Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, sequenceFromFile("svg/complete-fixed-sequence.svg"))
  }

  test("multi activity") {

    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) ::
        USER.call("finalize", SYSTEM) ::
        SYSTEM.reply("finalize done", USER) ::
        Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, sequenceFromFile("svg/multi-activity.svg"))
  }

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
        Nil
    )

    val canvas = viewer.view(flow.toEventBook)

    shouldBeLike(canvas, sequenceFromFile("svg/multi-actor.svg"))
  }

  private def shouldBeLike(canvas: Canvas, sequence: String): Any = {
    println(canvas)
    canvas.print().replaceAll("\n", "") shouldBe sequence.replaceAll("\n\\s*", "")
  }

  private def sequenceFromFile(filename: String) = {
    Source.fromResource(s"$filename").mkString
  }
}
