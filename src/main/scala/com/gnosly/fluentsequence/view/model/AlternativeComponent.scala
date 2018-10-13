package com.gnosly.fluentsequence.view.model
case class AlternativeComponent(id: Int, condition: String, startIndex: Int, var endIndex: Int = -1) {
  def end(index: Int): Unit = {
    endIndex = index
  }
}
