name := "futils"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % "0.13.2a",
  "org.http4s" %% "http4s-blaze-client" % "0.13.2a",
  "org.http4s" %% "http4s-dsl" % "0.13.2a",
  "org.http4s" %% "http4s-circe" % "0.13.2a",

  "io.circe" %% "circe-core" % "0.4.1",
  "io.circe" %% "circe-parser" % "0.4.1",

  "com.lihaoyi" %% "ammonite-ops" % "0.5.8",

  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
  "org.scalatra.rl" %% "rl" % "0.4.10",

  "org.scalaz" %% "scalaz-core" % "7.2.2",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.2"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

exportJars := true

lazy val json4sDrafts = ProjectRef(uri("https://github.com/dozed/json4s-drafts.git"), "json4s-drafts")

lazy val futils = project.in(file("."))
  .dependsOn(json4sDrafts)
