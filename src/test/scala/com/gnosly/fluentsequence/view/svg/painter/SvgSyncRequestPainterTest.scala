package com.gnosly.fluentsequence.view.svg.painter
import com.gnosly.fluentsequence.view.contracttest.SyncRequestPainterTest

class SvgSyncRequestPainterTest extends SyncRequestPainterTest {
  val painter = new SvgSyncRequestPainter

  forwardTest(
    painter,
    () =>
      """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="0" width="0"><rect width="100%" height="100%" fill="white"/>
				|<text x="40" y="0" font-size="16px" text-anchor="start">sync request</text>
				|<line x1="-10" y1="10" x2="198" y2="10" style="stroke:black;stroke-width:1.5;" />
				|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="188,5 198,10 188,15"/>
				|</svg>
			|""".stripMargin
  )

  backwardTest(
    painter,
    () =>
      """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="0" width="0"><rect width="100%" height="100%" fill="white"/>
				|<text x="40" y="0" font-size="16px" text-anchor="start">sync request</text>
				|<line x1="-8" y1="10" x2="200" y2="10" style="stroke:black;stroke-width:1.5;" />
				|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="2,5 -8,10 2,15"/>
				|</svg>
				|""".stripMargin
  )
}
