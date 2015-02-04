// Comment to get more information during initialization
logLevel := Level.Warn

resolvers ++= Seq(
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
	"Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
	"jgit-repo" at "http://download.eclipse.org/jgit/maven",
	Classpaths.sbtPluginReleases
)

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.0-RC2")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "0.99.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

