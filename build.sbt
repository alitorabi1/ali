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

