package com.gnosly.fluentsequence.view.model

class BiSignalComponent(val name: String,
												val index: Int,
												val fromActorId: Int,
												val toActorId: Int) extends SignalComponent(index){

	def canEqual(other: Any): Boolean = other.isInstanceOf[BiSignalComponent]

	override def equals(other: Any): Boolean = other match {
		case that: BiSignalComponent =>
			(that canEqual this) &&
				name == that.name &&
				index == that.index &&
				fromActorId == that.fromActorId &&
				toActorId == that.toActorId
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(name, index, fromActorId, toActorId)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}


	override def toString = s"BiSignalComponent($name, $index, ${fromActorId}, ${toActorId})"
}
