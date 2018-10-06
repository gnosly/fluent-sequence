package com.gnosly.fluentsequence.view.model
import com.gnosly.fluentsequence.view.model.component.Component

case class AlternativeComponent(val condition: String, val startIndex: Int, var endIndex: Int = -1) extends Component {
  def end(index: Int): Unit = {
    endIndex = index
  }
}
