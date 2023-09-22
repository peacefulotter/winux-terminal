name := "winux-terminal-backend"
organization := "peacefulotter.com"

version := "1.0"

lazy val root = (project in file("."))
    .enablePlugins(PlayScala, AshScriptPlugin)
    .settings()

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.9.1"
// libraryDependencies += "com.github.tototoshi" %% "play-json-generic" % "0.1.2"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

Docker / maintainer := "nmaire@peacefulotter.com"
Docker / packageName := "winux-terminal-backend"
Docker / version := sys.env.getOrElse("BUILD_NUMBER", "0")
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:8-jre-alpine"
dockerUpdateLatest := true
dockerEntrypoint := Seq("./bin/play-scala-seed")
// dockerEntrypoint := Seq("")
