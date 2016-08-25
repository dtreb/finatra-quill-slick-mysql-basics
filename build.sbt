import sbt.Keys._

name := """finatra-quill-slick-mysql-basics"""
version := "1.1"
scalaVersion := "2.11.8"

lazy val versions = new {
  val finatra = "2.2.0"
  val logback = "1.1.7"
  val guice = "4.0"
  val getquill = "0.5.0"
  val slickJoda = "2.1.0"
  val mysqljdbc = "5.1.37"
  val typesafeConfig = "1.3.0"
  val mockito = "1.9.5"
  val scalatest = "2.2.6"
  val specs2 = "2.3.12"
  val ficus = "1.1.2"
  val slick = "3.0.0"
  val hikaricp = "2.4.5"
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
  "com.twitter" % "bijection-util_2.11" % "0.9.2",

  // quill
  "io.getquill" %% "quill-finagle-mysql" % versions.getquill,
  // slick
  "com.typesafe.slick" %% "slick" % versions.slick,
  // Connection pool (for both quill and slick)
  "com.zaxxer" % "HikariCP" % versions.hikaricp,
  // MySQL driver
  "mysql" % "mysql-connector-java" % versions.mysqljdbc,

  // others
  "ch.qos.logback" % "logback-classic" % versions.logback,
  "com.typesafe" % "config" % versions.typesafeConfig,
  "net.ceedubs" % "ficus_2.11" % versions.ficus,

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
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.3" % "test",
  "org.specs2" %% "specs2" % "2.3.12" % "test")