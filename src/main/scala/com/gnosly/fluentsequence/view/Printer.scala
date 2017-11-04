package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.fixedwidth.Console

trait Printer {
	def print(eventBook: EventBook):Console
}
