package com.gnosly.fluentsequence.api

import com.gnosly.fluentsequence.core.EventBook

trait EventBookable {
  def toEventBook: EventBook
}
