ThisBuild / organization := "com.gnosly"
ThisBuild / organizationName := "Gnosly"
ThisBuild / organizationHomepage := Some(url("http://gnosly.com/"))

ThisBuild / scmInfo := Some(
	ScmInfo(
		url("https://github.com/gnosly/fluent-sequence"),
		"scm:git@github.com/gnosly/fluent-sequence.git"
	)
)
ThisBuild / developers := List(
	Developer(
		id    = "gnosly",
		name  = "Fabrizio Giovannetti",
		email = "fabriziogiovannetti@gmail.com",
		url   = url("http://fabriziogiovannetti.com")
	)
)

ThisBuild / description := "This project aims to create sequence diagrams programmatically, using a programming language like Scala or Java or a customized language. So you could save the created sequence as SVG image or fixed-width text."
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/gnosly/fluent-sequence"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
	val nexus = "https://oss.sonatype.org/"
	if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
	else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true