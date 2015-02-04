name := "so-webapp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.scalatestplus" % "play_2.10" % "1.0.0" % "test",
	"com.socialorra" % "so-api_2.10" % "0.1.0-SNAPSHOT"
) 

resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

play.Project.playScalaSettings

instrumentSettings

ScoverageKeys.minimumCoverage := 70

ScoverageKeys.failOnMinimumCoverage := false

ScoverageKeys.highlighting := {
  if (scalaBinaryVersion.value == "2.10") false
	else false
}

publishArtifact in Test := false

parallelExecution in Test := false
