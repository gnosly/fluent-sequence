package com.gnosly.fluentsequence.view.model.point

case class Fixed1DPoint(x: Long) extends Point1d {
  override def resolve(pointMap: ResolvedPoints): Fixed1DPoint = this

  def <(other: Fixed1DPoint): Boolean = this.x < other.x

  def <=(other: Fixed1DPoint): Boolean = this.x <= other.x

  def max(that: Fixed1DPoint): Fixed1DPoint = this.x.max(that.x) match {
    case this.x => this
    case _      => that
  }
  override def toString: String = s"${x}"
}
