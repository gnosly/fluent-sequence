package com.gnosly.fluentsequence.view.fixedwidth

object Util {
	def middle(name: String, padding: Long): String = {
		if (padding <= 1) {
			return name
		}

		" " + middle(name, padding - 2) + " "
	}

	def r(pattern: String, count: Long): String =
		(0 until count.toInt).map(_ => pattern).reduce(_ + _)
}
