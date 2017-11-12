package com.gnosly.fluentsequence.view.model

case class ActorView(name:String, x:Int) {
	var activity:Activity = null
	var liveness:Liveness = null

	def startOrContinue(index: Int) = {
		activity = Activity(x, TimelineStep(index, index+1))
		liveness = Liveness(x, activity.live.to)
	}

}
