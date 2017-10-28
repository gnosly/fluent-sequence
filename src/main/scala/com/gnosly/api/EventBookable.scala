package com.gnosly.api

import com.gnosly.fluentsequence.core.EventBook

trait EventBookable {
	def toEventBook: EventBook
}
