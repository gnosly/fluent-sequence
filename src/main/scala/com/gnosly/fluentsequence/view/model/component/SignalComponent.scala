package com.gnosly.fluentsequence.view.model.component

class SignalComponent(index: Int, val fromActorId: Int, val fromActivityId: Int) extends Component {
  def currentIndex() = index
}
