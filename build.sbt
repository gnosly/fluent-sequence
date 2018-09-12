name := "fluent-sequence"
organization := "com.gnosly"

scalaVersion := "2.12.6"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
	checkSnapshotDependencies,
	inquireVersions,
	runClean,
	runTest,
	setReleaseVersion,
	commitReleaseVersion,
	tagRelease,
	releaseStepTask(PgpKeys.publishSigned),
	setNextVersion,
	commitNextVersion,
	releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease),
	pushChanges
)