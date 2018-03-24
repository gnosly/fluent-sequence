package com.gnosly.fluentsequence.view

import com.gnosly.fluentsequence.core.EventBook
import com.gnosly.fluentsequence.view.model.{ViewModelComponents, ViewModelComponentsGenerator}

class Viewer {
	def view(eventBook: EventBook): String = {
		val viewModel: ViewModelComponents = ViewModelComponentsGenerator.generate(eventBook)
		viewModel
		return ""
	}
}
