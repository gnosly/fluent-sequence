package com.gnosly.fluentsequence.view.fixedwidth.painter

import com.gnosly.fluentsequence.view.Coordinates._
import com.gnosly.fluentsequence.view.fixedwidth.{Fixed2dPoint, FixedWidthCanvas, FixedWidthPreRenderer, FormatterConstants}
import com.gnosly.fluentsequence.view.model._
import com.gnosly.fluentsequence.view.model.component._
import com.gnosly.fluentsequence.view.{Canvas, Painter}

class FixedWidthPainter extends Painter {
	val actorPainter = new FixedWidthActorPainter()
	val preRenderer = new FixedWidthPreRenderer()

	override def paint(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Canvas = {
		val canvas = new FixedWidthCanvas()
		paintTitle(viewModel, pointMap, canvas)

		val actorCanvas = viewModel._actors.map(
			a => actorPainter.paint(a._2, pointMap)
		)

		viewModel._actors.flatMap(_._2.activities).foreach(
			a => {

				if (a.isFirst()) {
					val timelineStart = pointMap(Actor.bottomMiddle(a.actorId))
					val topLeftActivity = pointMap(Activity.topLeft(a.actorId, a.id))
					0L until topLeftActivity.y - timelineStart.y foreach (i => canvas.write(timelineStart.down(i), "|"))
				} else {
					val previousBottomLeftActivity = pointMap(Activity.bottomLeft(a.actorId, a.id - 1))
					val topLeftActivity = pointMap(Activity.topLeft(a.actorId, a.id))

					1L until topLeftActivity.y - previousBottomLeftActivity.y foreach (i => canvas.write(previousBottomLeftActivity.down(i), "|"))
				}

				val existNextActivity = pointMap.contains(Activity.bottomLeft(a.actorId, a.id + 1))
				if (!existNextActivity) { // not exist //todo
					val bottomLeft = pointMap(Activity.bottomLeft(a.actorId, a.id))
					canvas.write(bottomLeft.down(1).right(1), "|")
				}

				paint(a, canvas, pointMap)
			}
		)

		return actorCanvas.reduce(_.merge(_)).merge(canvas)
	}

	private def paintTitle(viewModel: ViewModelComponents, pointMap: Map[String, Fixed2dPoint], canvas: FixedWidthCanvas) = {
		val sequenceWidth = allColumnWidth(viewModel, pointMap)
		val sequenceHeight = pointMap(ViewMatrix.row(viewModel.lastSignalIndex)).x + 3

		val component = viewModel.sequenceComponents(0)
		val sequenceTitle = component.name
		canvas.write(Fixed2dPoint(0, 0), r("_", sequenceTitle.length + 3))
		canvas.write(Fixed2dPoint(0, 1), s"| ${sequenceTitle} \\")
		canvas.write(Fixed2dPoint(0, 2), "|" + r("-", sequenceWidth))
		canvas.write(Fixed2dPoint(0, sequenceHeight), "|" + r("_", sequenceWidth))

		for (y <- 3 until sequenceHeight.toInt) {
			canvas.write(Fixed2dPoint(0, y), "|")
			canvas.write(Fixed2dPoint(sequenceWidth, y), "|")
		}
	}

	private def paint(activity: ActivityComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
		val topLeftActivity = pointMap(Activity.topLeft(activity.actorId, activity.id))
		val bottomLeftActivity = pointMap(Activity.bottomLeft(activity.actorId, activity.id))

		canvas.write(topLeftActivity, "_|_")

		val activityStart = topLeftActivity.down(1)
		for (i <- 0L to bottomLeftActivity.up(1).y - activityStart.y) {
			canvas.write(activityStart.down(i), "| |")
		}

		canvas.write(bottomLeftActivity, "|_|")

		//print right signal
		for (rightPoint <- activity.rightPoints) {
			rightPoint._2.signalComponent match {
				case x: AutoSignalComponent => paint(x, canvas, pointMap)
				case x: BiSignalComponent => paint(x, canvas, pointMap)
			}
		}
	}

	private def paint(autoSignal: AutoSignalComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
		val signalPoint = pointMap(Activity.rightPointStart(autoSignal.actorId, autoSignal.activityId, autoSignal.currentIndex()))

		canvas.write(signalPoint, "____")
		canvas.write(signalPoint.down(1), "    |")
		canvas.write(signalPoint.down(2), "    | " + autoSignal.name)
		canvas.write(signalPoint.down(3), "<---'")
	}

	private def paint(biSignal: BiSignalComponent, canvas: FixedWidthCanvas, pointMap: Map[String, Fixed2dPoint]): Unit = {
		if (biSignal.leftToRight()) {
			val signalPoint = pointMap(Activity.rightPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex()))
			val leftActivityPoint = pointMap(Activity.leftPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex()))
			val distance = leftActivityPoint.x - signalPoint.x

			canvas.write(signalPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
			canvas.write(signalPoint.down(1), r("-", distance - 1) + ">")
		} else {
			val signalLeftPoint = pointMap(Activity.rightPointStart(biSignal.toActorId, biSignal.toActivityId, biSignal.currentIndex()))
			val signalRightPoint = pointMap(Activity.leftPointStart(biSignal.fromActorId, biSignal.fromActivityId, biSignal.currentIndex()))
			val distance = signalRightPoint.x - signalLeftPoint.x

			canvas.write(signalLeftPoint.right((distance - biSignal.name.length) / 2), biSignal.name)
			canvas.write(signalLeftPoint.down(1), "<" + r("-", distance - 1))
		}
	}

	def r(pattern: String, count: Long): String =
		(0 until count.toInt).map(_ => pattern).reduce(_ + _)

	private def allColumnWidth(viewModelComponents: ViewModelComponents, pointMap: Map[String, Fixed2dPoint]): Long = {
		val sequenceWidth = 3
		//FIXME: move into viewModel
		val count = viewModelComponents._actors.foldLeft(0L)((z, a) => {
			z + sequenceWidth + pointMap(ViewMatrix.column(a._2.id)).x
		})

		FormatterConstants.LEFT_MARGIN + count
	}
}