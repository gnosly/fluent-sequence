package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook

trait Printer {
	def print(eventBook: EventBook):Printable
}
