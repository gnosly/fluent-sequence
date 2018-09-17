package com.gnosly.fluentsequence.view.model.point

class ReferencePoint(referenceName: String)
    extends Variable2DPoint(Reference1DPoint(s"$referenceName#x"), Reference1DPoint(s"$referenceName#y")) {}
