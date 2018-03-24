package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.Canvas
import com.gnosly.fluentsequence.view.model.ActorComponent

import scala.collection.mutable

class FixedWidthCanvas extends Canvas {

	private val canvas = mutable.HashMap[Fixed2DPoint, Character]()

	override def print(): String = ???

	override def write(actorComponent: ActorComponent, map: Map[String, Long]): Unit = {

	}

	case class Fixed2DPoint(x: Long, y: Long)

}












