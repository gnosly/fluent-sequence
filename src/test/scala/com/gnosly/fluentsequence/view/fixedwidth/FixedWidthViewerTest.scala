package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.api.FluentSequence.FluentActor
import com.gnosly.fluentsequence.api.FluentSequence.Sequence
import com.gnosly.fluentsequence.api.FluentSequence.User
import com.gnosly.fluentsequence.api.FluentSequence.inCase
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.io.Source

class FixedWidthViewerTest extends FunSuite with Matchers {
  val USER = new User("user")
  val SYSTEM = new FluentActor("system")
  val viewer = new FixedWidthViewer

  test("view an actor that calls another") {
    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) :: Nil
    )

    val str = viewer.view(flow.toEventBook)
    println(str)
    str.print() shouldBe sequenceFromFile("two-actors-one-call.txt")
  }

  test("do a two actor sequence") {
    val flow = Sequence("example").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) :: Nil
    )

    val str = viewer.view(flow.toEventBook)
    println(str)
    str.print() shouldBe sequenceFromFile("two-actors.txt")
  }

  test("do a complete sequence") {

    val flow = Sequence("example").startWith(
      USER.does("something") ::
        USER.does("something else") ::
        USER.call("call", SYSTEM) ::
        SYSTEM.reply("reply", USER) :: Nil
    )

    val str = viewer.view(flow.toEventBook)
    println(str)

    str.print() shouldBe sequenceFromFile("complete-fixed-sequence.txt")
  }

  test("alternative") {

    val flow = Sequence("example").startWith(
      USER.does("abc") ::
        inCase("condition", USER.does("something") :: Nil) ::
        Nil
    )
    val str = viewer.view(flow.toEventBook)
    println(str)
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

    val str = viewer.view(flow.toEventBook)
    println(str)
    str.print() shouldBe sequenceFromFile("multi-activity.txt")
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
        USER.call("call4", thirdSystem) ::
        thirdSystem.call("call5", SYSTEM) ::
        SYSTEM.reply("reply4", thirdSystem) ::
        Nil
    )

    val str = viewer.view(flow.toEventBook)
    println(str)
    str.print() shouldBe sequenceFromFile("multi-actor.txt")
  }

  test("sub sequence") {

    val subSequence = Sequence("sub sequence")
      .startWith(SYSTEM.does("something") :: Nil)

    val parentFlow = Sequence("sequence parent").startWith(
      USER.call("call", SYSTEM) ::
        SYSTEM.does(subSequence) :: Nil
    )

    val str = viewer.view(parentFlow.toEventBook)
    println(str)
    str.print() shouldBe sequenceFromFile("sub-sequence.txt")
  }

  private def sequenceFromFile(filename: String) = {
    Source.fromResource(filename).mkString
  }
}
