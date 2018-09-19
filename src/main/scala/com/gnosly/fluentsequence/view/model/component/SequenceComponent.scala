package com.gnosly.fluentsequence.view.model.component

class SequenceComponent(val name: String, val startIndex: Int) extends Component {

  def canEqual(other: Any): Boolean = other.isInstanceOf[SequenceComponent]

  override def equals(other: Any): Boolean = other match {
    case that: SequenceComponent =>
      (that canEqual this) &&
        name == that.name &&
        startIndex == that.startIndex
    case _ => false
  }

  override def hashCode: Int = {
    val state = Seq(name, startIndex)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"SequenceComponent($name, $startIndex)"
}
