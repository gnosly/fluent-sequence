import xerial.sbt.Sonatype._

publishTo := sonatypePublishTo.value

sonatypeProjectHosting := Some(GitHubHosting(
	user = "gnosly",
	repository = name.value,
	fullName = "Fabrizio Giovannetti",
	email = "fabriziogiovannetti@gmail.com"
))

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))