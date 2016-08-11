import sbt.Keys._

name := """finatra-quill-mysql-basics"""
version := "1.0"
scalaVersion := "2.11.8"

lazy val versions = new {
  val finatra = "2.2.0"
  val logback = "1.1.7"
  val guice = "4.0"
  val getquill = "0.5.0"
  val hikaricp = "2.4.5"
  val slickJoda = "2.1.0"
  val mysqljdbc = "5.1.37"
  val jodaTime = "2.9.3"
  val jodaConvert = "1.8"
  val twitterUtil = "6.33.0"
  var twitterAsync = "516e77a"
  val typesafeConfig = "1.3.0"
  val mockito = "1.9.5"
  val scalatest = "2.2.6"
  val specs2 = "2.3.12"
  val ficus = "1.2.3" // for scala friendly typesafe config
  val async = "0.9.5"
}

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

// assembly for packaging as single jar
assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case other => MergeStrategy.defaultMergeStrategy(other)
}

assemblyJarName in assembly := s"${name.value}.jar"

libraryDependencies ++= Seq(

  // finatra
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-slf4j" % versions.finatra,
  "com.twitter" %% "finatra-httpclient" % versions.finatra,

  // quill
  "io.getquill" %% "quill-finagle-mysql" % versions.getquill,
  "com.zaxxer" % "HikariCP" % versions.hikaricp,
  "mysql" % "mysql-connector-java" % versions.mysqljdbc,
  "joda-time" % "joda-time" % versions.jodaTime,
  "org.joda" % "joda-convert" % versions.jodaConvert,

  // twitter async
  "com.github.foursquare" % "twitter-util-async" % versions.twitterAsync,

  // scala async
  "org.scala-lang.modules" %% "scala-async" %  versions.async,

  // typesafe config
  "com.typesafe" % "config" % versions.typesafeConfig,
  "com.iheart" %% "ficus" % versions.ficus, // for scala friendly typesafe config

  // reflect
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  // others
  "ch.qos.logback" % "logback-classic" % versions.logback,

  // test
  "com.twitter" %% "finatra-http" % versions.finatra % "test",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2" % versions.specs2 % "test"
)


Revolver.settings


fork in run := true