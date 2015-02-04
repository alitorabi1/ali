import sbt._
import Keys._
import play.PlayScala

object SoBuild extends Build {
  override lazy val settings = super.settings ++
    Seq(
      name := "socialorra",
      version := "1.0.0",
      organization := "com.socialorra",
      scalaVersion := "2.11.5",
      resolvers ++= Seq(Resolver.mavenLocal,
        "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
        "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
        "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")
    )

  import Dependencies._

  val apiDeps = Seq(
    apacheCommons,
    akkaActor,
    facebook4j,
    greenMail,
    scalaTest,
    twitter4j
  )

  val webappDeps = Seq(
    scalaTest
  )

  lazy val root = Project(id = "socialorra",
    base = file(".")) aggregate(api, webapp)

  lazy val api = Project(id = "api",
    base = file("api"),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies ++= apiDeps
    ) ++ Format.settings
  )

  lazy val webapp = Project(id = "webapp", base = file("webapp"))
    .enablePlugins(play.PlayScala)
    .settings(
      publishArtifact in Test := false,
      parallelExecution in Test := false,
      fork in run := false,
      //ScoverageKeys.minimumCoverage := 70,
      //ScoverageKeys.failOnMinimumCoverage := false,
      //ScoverageKeys.highlighting := { if (scalaBinaryVersion.value == "2.10") false else false },
      libraryDependencies ++= webappDeps
    )
    .dependsOn(api % "compile->compile;test->test")
}

object Dependencies {
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.3.5"
  val akkaAgent = "com.typesafe.akka" %% "akka-agent" % "2.3.5"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  val scalaReflect = "org.scala-lang" % "scala-reflect" % "2.11.5"
  val typesafeConfig = "com.typesafe" % "config" % "1.2.1"
  val scalaTestPlus = "org.scalatestplus" % "play_2.10" % "1.0.0" % "test"
  val config = "com.typesafe" % "config" % "1.2.0"
  val twitter4j = "org.twitter4j" % "twitter4j-core" % "4.0.1"
  val facebook4j = "org.facebook4j" % "facebook4j-core" % "2.1.0"
  val apacheCommons = "org.apache.commons" % "commons-email" % "1.3.2"
  val greenMail = "com.icegreen" % "greenmail" % "1.3.1b" % "test"
}

object Format {
  import com.typesafe.sbt.SbtScalariform._

  lazy val settings = scalariformSettings ++ Seq(
    ScalariformKeys.preferences := formattingPreferences
  )

  lazy val formattingPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences().
      setPreference(AlignParameters, true).
      setPreference(AlignSingleLineCaseStatements, true).
      setPreference(DoubleIndentClassDeclaration, true).
      setPreference(IndentLocalDefs, true).
      setPreference(IndentPackageBlocks, true).
      setPreference(IndentSpaces, 2).
      setPreference(MultilineScaladocCommentsStartOnFirstLine, true).
      setPreference(PreserveSpaceBeforeArguments, false).
      setPreference(PreserveDanglingCloseParenthesis, false).
      setPreference(RewriteArrowSymbols, true).
      setPreference(SpaceBeforeColon, false).
      setPreference(SpaceInsideBrackets, false).
      setPreference(SpacesWithinPatternBinders, true)
  }
}
