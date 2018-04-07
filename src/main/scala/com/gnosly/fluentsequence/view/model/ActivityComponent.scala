package com.gnosly.fluentsequence.view.model

case class ActivityComponent(id: Int, fromIndex: Int, var toIndex: Int, var active: Boolean = false) extends Component {
	def end(index: Int): Unit = {
		if (active) {
			increaseUntil(index)
			active = false
		}
	}

	def increaseUntil(index: Int): Unit = {
		toIndex = index
	}
}
