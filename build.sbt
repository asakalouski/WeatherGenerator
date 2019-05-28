name := "WeatherGenerator"

version := "0.1"

scalaVersion := "2.12.8"

fork in run := true
cancelable in Global := true

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.23"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.23" % Test
