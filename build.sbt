ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "AwsConnectionWithLocalstack"
  )

libraryDependencies ++= Seq("com.amazonaws" % "aws-java-sdk-core" % "1.12.349",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.12.349",
  "com.amazonaws" % "aws-java-sdk-sqs" % "1.12.349",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "com.github.pureconfig" %% "pureconfig" % "0.14.0",
  "org.scalatest" % "scalatest_2.13" % "3.2.12",
  "org.mockito" % "mockito-scala_2.13" % "1.17.5",
  "cloud.localstack" % "localstack-utils" % "0.2.1")
