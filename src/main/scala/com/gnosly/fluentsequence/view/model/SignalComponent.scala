package com.gnosly.fluentsequence.view.model

class SignalComponent(index: Int, val fromActorId: Int, val fromActivityId:Int) extends Component{
	def currentIndex() = index
}
