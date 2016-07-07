name := "livescrape"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
    "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2",
    "nu.validator.htmlparser" % "htmlparser" % "1.2.1",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
)

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
