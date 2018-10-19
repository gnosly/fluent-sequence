package com.gnosly.fluentsequence.view.svg.painter
import com.gnosly.fluentsequence.view.contracttest.SyncResponsePainterTest

class SvgSyncResponsePainterTest extends SyncResponsePainterTest(new SvgSyncResponsePainter) {

  override def backwardExpected: String =
    """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="0" width="0"><rect width="100%" height="100%" fill="white"/>
			|<text x="30" y="0" font-size="16px" text-anchor="start">sync response</text>
			|<line x1="-8" y1="10" x2="200" y2="10" style="stroke:black;stroke-width:1.5;" />
			|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="2,5 -8,10 2,15"/>
			|</svg>
			|""".stripMargin
  override def forwardExpected: String =
    """<svg xmlns="http://www.w3.org/2000/svg" version="1.1" height="0" width="0"><rect width="100%" height="100%" fill="white"/>
			|<text x="30" y="0" font-size="16px" text-anchor="start">sync response</text>
			|<line x1="-10" y1="10" x2="198" y2="10" style="stroke:black;stroke-width:1.5;" />
			|<polyline fill="none" stroke="black" stroke-width="1.5" stroke-linecap="square" stroke-linejoin="miter" points="188,5 198,10 188,15"/>
			|</svg>
			|""".stripMargin
}
