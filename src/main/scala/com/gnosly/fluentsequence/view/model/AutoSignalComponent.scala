package com.gnosly.fluentsequence.view.model

class AutoSignalComponent(val name: String,
													val index: Int,
													val actorId: Int,
													val activityId:Int) extends SignalComponent(index, actorId, activityId){

	def canEqual(other: Any): Boolean = other.isInstanceOf[AutoSignalComponent]


	override def equals(other: Any): Boolean = other match {
		case that: AutoSignalComponent =>
			(that canEqual this) &&
				name == that.name &&
				index == that.index &&
				actorId == that.actorId
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(name, index, actorId)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}

	override def toString = s"AutoSignalComponent($name, $index, ${actorId})"
}
