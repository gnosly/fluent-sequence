package com.gnosly.fluentsequence.view.model

case class Box(width: Long, height: Long) {
  def halfWidth: Long = width / 2
}
