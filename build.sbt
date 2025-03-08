ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

val catsVersion     = "2.12.0"
val catsEffectVer   = "3.5.3"
val http4sVersion       = "0.23.26"
val jsoupVersion       = "1.17.2"
val logbackVersion      = "1.5.6"
val log4catsVersion    = "2.7.0"
val typelevelCats      = "2.10.0"
val circeVersion       = "0.14.6"
val catsEffectVersion  = "3.5.3"

libraryDependencies ++= Seq(
  // Cats
  "org.typelevel" %% "cats-core" % typelevelCats,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,

  // создание HTTP сервера и клиента
  "org.http4s" %% "http4s-ember-server" % "0.23.26",
  "org.http4s" %% "http4s-ember-client" % "0.23.26",
  "org.http4s" %% "http4s-dsl"          % "0.23.26",
  "org.http4s" %% "http4s-circe"        % "0.23.26",

  // Circe для работы с JSON
  "io.circe" %% "circe-generic" % circeVersion,

  // для парсинга HTML
  "org.jsoup" % "jsoup" % jsoupVersion,

  // Логирование
  "org.typelevel" %% "log4cats-slf4j" % log4catsVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,

  // для синтаксиа cats io
  "org.typelevel" %% "cats-core" % typelevelCats,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
)
lazy val root = (project in file("."))
  .settings(
    name := "linkapp"
  )
