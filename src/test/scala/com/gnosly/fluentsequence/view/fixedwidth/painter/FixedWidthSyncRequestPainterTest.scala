package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.SyncRequestPainterTest
import org.scalatest.Matchers

class FixedWidthSyncRequestPainterTest extends SyncRequestPainterTest {

  val painterUnderTest = new FixedWidthSyncRequestPainter()

  forwardTest(painterUnderTest,
              () =>
                /****/ "    sync request" + "\n" +
                  /**/ "------------------->")

  backwardTest(painterUnderTest,
               () =>
                 /****/ "    sync request" + "\n" +
                   /**/ "<-------------------")
}
