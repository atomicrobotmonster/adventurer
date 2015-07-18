lazy val root = (project in file(".")).
  settings(
    name := "adventurer", 
    version := "0.1",
    scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-Xlint", "-unchecked", "-deprecation")
)
