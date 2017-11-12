package com.gnosly.fluentsequence.view.model

case class ActorView(name:String, x:Step) {
	var activity:Activity = null
	var liveness:Liveness = null

	def startOrContinue(index: Int) = {
		activity = Activity(x, TimelineStep(Step(index), Step(index+1)))
		liveness = Liveness(x, activity.live.to)
	}

}
