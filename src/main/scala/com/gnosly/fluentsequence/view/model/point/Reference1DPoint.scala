package com.gnosly.fluentsequence.view.model.point

case class Reference1DPoint(name: String) extends Point1d {
  override def resolve(pointMap: ResolvedPoints): Fixed1DPoint = {
    if (name.split("#").length > 1) {
      val point = name.split("#")(0)
      val coordinate = name.split("#")(1)

      if (coordinate == "x") {
        Fixed1DPoint(pointMap(point).x)
      } else {
        Fixed1DPoint(pointMap(point).y)
      }
    } else {
      Fixed1DPoint(pointMap(name).x) // x for convention
    }
  }
  override def toString: String = s"'${name}'"
}
