package com.gnosly.fluentsequence.view.model

import scala.collection.mutable

class ActorComponent(val id: Int, val name: String, val activities: mutable.Buffer[ActivityComponent]) extends Component {
	def this(column: Int, name: String, activity: ActivityComponent) {
		this(column, name, mutable.Buffer(activity))
	}

	def this(column: Int, name: String, fromIndex: Int) {
		this(column, name, new ActivityComponent(0, fromIndex, 0, true))
	}

	def done(something: String, index: Int): SignalComponent = {
		val lastActivity = this.activeUntil(index)
		val autoSignal = new AutoSignalComponent(something, index, this.id)
		lastActivity.right(autoSignal)
		autoSignal
	}

	def link(called: ActorComponent, something: String, index: Int): SignalComponent = {
		val lastCallerActivity = this.activeUntil(index)
		val lastCalledActivity = called.activeUntil(index)
		val signal = new BiSignalComponent(something, index, this.id, called.id)
		if(this.id < called.id){
			lastCallerActivity.right(signal)
			lastCalledActivity.left(signal)
		}else{
			lastCallerActivity.left(signal)
			lastCalledActivity.right(signal)
		}
		signal
	}

	def activeUntil(index: Int): ActivityComponent = {
		val last = activities.last
		if (last.active) {
			last.increaseUntil(index)
			last
		}
		else {
			val component = new  ActivityComponent(last.id + 1, index, 0, true)
			activities += component
			component
		}
	}

	def end(index: Int): Unit = {
		activities.last.end(index)
	}


	def canEqual(other: Any): Boolean = other.isInstanceOf[ActorComponent]

	override def equals(other: Any): Boolean = other match {
		case that: ActorComponent =>
			(that canEqual this) &&
				id == that.id &&
				name == that.name &&
				activities == that.activities
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(id, name, activities)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}


	override def toString = s"ActorComponent($id, $name, $activities)"
}
