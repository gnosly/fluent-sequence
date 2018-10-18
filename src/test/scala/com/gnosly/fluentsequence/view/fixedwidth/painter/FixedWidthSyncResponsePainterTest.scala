package com.gnosly.fluentsequence.view.fixedwidth.painter
import com.gnosly.fluentsequence.view.contracttest.SyncResponsePainterTest

class FixedWidthSyncResponsePainterTest extends SyncResponsePainterTest(new FixedWidthSyncResponsePainter) {
  override def backwardExpected: String = {

    /****/
    "   sync response" + "\n" +
      /**/ "<-------------------"
  }

  override def forwardExpected: String = {

    /****/
    "   sync response" + "\n" +
      /**/ "------------------->"
  }
}
