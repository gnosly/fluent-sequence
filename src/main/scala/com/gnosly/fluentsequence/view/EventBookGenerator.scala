package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook

trait EventBookGenerator[T] {

	def generate(eventBook:EventBook):T
}
