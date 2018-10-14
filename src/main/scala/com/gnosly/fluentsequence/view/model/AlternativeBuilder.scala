package com.gnosly.fluentsequence.view.model
import com.gnosly.fluentsequence.view.model.ViewModels.AlternativeModel

class AlternativeBuilder(id: Int, condition: String, startIndex: Int, var endIndex: Int = -1) {

  def end(index: Int): Unit = {
    endIndex = index
  }

  def build(): AlternativeModel = {
    AlternativeModel(id, condition, startIndex, endIndex)
  }
}
