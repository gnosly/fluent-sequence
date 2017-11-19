package com.gnosly.fluentsequence.view.fixedwidth

object Util {
	def middle(name: String, padding: Int): String = {
		if (padding <= 1) {
			return name
		}

		" " + middle(name, padding - 2) + " "
	}

	def r(pattern: String, count: Int): String =
		(0 until count).map(_ => pattern).reduce(_ + _)
}
