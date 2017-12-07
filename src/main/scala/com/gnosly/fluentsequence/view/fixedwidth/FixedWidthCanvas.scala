package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.Matrix
import com.gnosly.fluentsequence.view.model.boxable.ActorBox
import com.gnosly.fluentsequence.view.{Canvas, MatrixView}

class FixedWidthCanvas extends Canvas {
	var out: String = ""

	def build(matrix: Matrix) = {
		val sizes = new MatrixView()

		//qualcuno decidera l'ordine degli attori
		val actorBoxes = matrix._actors.values.map(a => ActorBox(a).out)
		out += (actorBoxes.toList.apply(0))
	}
}

//larghezza (*)
//nome actor
//autoSignal
//BiSignal / 2
//

//altezza[*]
//AutoSignal
//Bisignal * numero

//ogni intero index (colonna) => dimensione larghezza => max(*)
//ogni intero index (riga) => dimensione altezza => max[*]
//quindi ho una matrice delle dimensioni
//Creo i pezzi
//li metto in una matrice di celle pixellose (ogni componente sa adattarsi per renderizzarsi secondo le dimensioni)
//Stampo la matrice riga per riga













