package com.gnosly.fluentsequence.view.model

class SequenceComponent(val name:String) extends Component {

	def canEqual(other: Any): Boolean = other.isInstanceOf[SequenceComponent]

	override def equals(other: Any): Boolean = other match {
		case that: SequenceComponent =>
			(that canEqual this) &&
				name == that.name
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(name)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}
}
