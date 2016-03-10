name := "futils"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % "0.12.1",
  "org.http4s" %% "http4s-blaze-client" % "0.12.1",
  "org.http4s" %% "http4s-dsl" % "0.12.1",

  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",

  "org.scalaz" %% "scalaz-core" % "7.1.7",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.7",

  "org.json4s" %% "json4s-scalaz" % "3.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",

  "com.chuusai" %% "shapeless" % "2.3.0",
  "com.github.julien-truffaut" %% "monocle-core" % "1.2.0-M1"
)

// https://github.com/non/kind-projector
// makes writing type signatures easier
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

