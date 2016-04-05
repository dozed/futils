name := "futils"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % "0.13.0a",
  "org.http4s" %% "http4s-blaze-client" % "0.13.0a",
  "org.http4s" %% "http4s-dsl" % "0.13.0a",

  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
  "org.scalatra.rl" %% "rl" % "0.4.10",

  "org.scalaz" %% "scalaz-core" % "7.2.2",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.2"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

lazy val json4sDrafts = ProjectRef(uri("ssh://git@github.com/dozed/json4s-drafts.git"), "json4s-drafts")

lazy val futils = project.in(file("."))
  .dependsOn(json4sDrafts)
