addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.5")

//addSbtPlugin("com.typesafe.sbt" % "sbt-atmos" % "0.3.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.0-RC2")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.0-SNAPSHOT")

resolvers ++= Seq(
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
	"Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
	"jgit-repo" at "http://download.eclipse.org/jgit/maven"
)

resolvers += Classpaths.sbtPluginReleases

