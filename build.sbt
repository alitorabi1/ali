name := "so-webapp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.scalatestplus" % "play_2.10" % "1.0.0" % "test"
)     

play.Project.playScalaSettings
