package com.gnosly.fluentsequence.view.svg

import com.gnosly.fluentsequence.view.contracttest.ViewerTest

class SvgViewerTest extends ViewerTest(new SvgViewer) {
  override def viewAnActorThatCallsAnotherExpected(): String = sequenceFromFile("svg/two-actors-one-call.svg")
  override def doATwoActorSequenceExpected(): String = sequenceFromFile("svg/two-actors.svg")
  override def doACompleteSequenceExpected(): String = sequenceFromFile("svg/complete-fixed-sequence.svg")
  override def multiActivityExpected(): String = sequenceFromFile("svg/multi-activity.svg")
  override def multiActorExpected(): String = sequenceFromFile("svg/multi-actor.svg")
}
