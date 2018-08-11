package com.gnosly.fluentsequence.view.svg.painter

import com.gnosly.fluentsequence.view.model.ViewModelComponents
import com.gnosly.fluentsequence.view.model.component.{AutoSignalComponent, BiSignalComponent}
import com.gnosly.fluentsequence.view.{Canvas, Fixed2dPoint, Painter}

//fixme maybe could be one painter
case class SvgPainter() extends Painter{
	val actorPainter = new SvgActorPainter()
	val activityPainter = new SvgActivityPainter()
	val biSignalPainter = new SvgBiSignalPainter()
	val autoSignalPainter = new SvgAutoSignalPainter()

	override def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas = {

		val actorCanvas = viewModel._actors.map(a => actorPainter.paint(a._2, pointMap))

		val activityCanvas = for {
			a <- viewModel._actors
			activity <- a._2.activities
		} yield activityPainter.paint(activity, pointMap)

		val signalCanvas = for {
			a <- viewModel._actors
			activity <- a._2.activities
			rightPoint <- activity.rightPoints
		} yield rightPoint._2.signalComponent match {
			case x: AutoSignalComponent => autoSignalPainter.paint(x, pointMap)
			case x: BiSignalComponent => biSignalPainter.paint(x, pointMap)
		}

		return (actorCanvas ++ activityCanvas ++ signalCanvas)
			.reduce(_.merge(_))
	}
}
