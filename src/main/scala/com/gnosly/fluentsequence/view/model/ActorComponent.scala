package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

case class ActorComponent(column: Int, name: String, var activities: mutable.Buffer[ActivityComponent]) extends Component {
	def done(something: String, index: Int): SignalComponent = {
		val lastActivity = this.activeUntil(index)
		val autoSignal = AutoSignalComponent(something, index, this)
		lastActivity.right(autoSignal)
		autoSignal
	}

	def link(called: ActorComponent, something: String, index: Int): SignalComponent = {
		val lastCallerActivity = this.activeUntil(index)
		val lastCalledActivity = called.activeUntil(index)
		val signal = BiSignalComponent(something, index, this, called)
		lastCallerActivity.right(signal)
		lastCalledActivity.left(signal)
		signal
	}

	def activeUntil(index: Int): ActivityComponent = {
		val last = activities.last
		if (last.active) {
			last.increaseUntil(index)
			last
		}
		else {
			val component = ActivityComponent(last.id + 1, index, 0, true)
			activities += component
			component
		}
	}

	def this(column: Int, name: String, activity: ActivityComponent) {
		this(column, name, mutable.Buffer(activity))
	}

	def this(column: Int, name: String, fromIndex: Int) {
		this(column, name, ActivityComponent(0, fromIndex, 0, true))
	}

	def end(index: Int): Unit = {
		activities.last.end(index)
	}

	def topLeftCornerId() = s"actor_${column}_top_left"
}
