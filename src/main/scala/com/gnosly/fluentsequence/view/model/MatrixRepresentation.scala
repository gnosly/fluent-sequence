package com.gnosly.fluentsequence.view.model

case class MatrixRepresentation(val actors: List[ActorView],
																val activities: List[Activity],
																val signals: List[Signal],
																val livenesses: List[Liveness]) {


}
