package com.gnosly.fluentsequence.view.fixedwidth

import com.gnosly.fluentsequence.view.model.boxable.{ActorBox, AutoSignalBox, Boxable}
import com.gnosly.fluentsequence.view.model.{ActorComponent, AutoSignalComponent, ViewModelComponents}
import com.gnosly.fluentsequence.view.{Canvas, MatrixView}

class FixedWidthCanvas extends Canvas {
	var out: String = ""

	def build(matrix: ViewModelComponents) = {
		val sizes = new MatrixView()

		//l'ordine degli attori è deciso in costruzione della matrice
		val actorBoxes = matrix._actors.values.map(a => ActorBox(a).out)
		out += (actorBoxes.toList.apply(0))


	}


	def boxes(matrix: ViewModelComponents): Iterable[Boxable] ={
		val actorBoxes = matrix._actors.values.map(a => ActorBox(a))
		 val signalBoxes = matrix._signals.map(s => AutoSignalBox(s.asInstanceOf[AutoSignalComponent]))

		actorBoxes ++ signalBoxes
	}

	override def print(): String = ???

	override def write(actorComponent: ActorComponent, map: Map[String, Long]): Unit = ???
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













