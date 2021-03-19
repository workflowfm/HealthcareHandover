lazy val pew = "com.workflowfm" %% "pew" % "1.5.0"
lazy val sim = "com.workflowfm" %% "proter" % "0.6"
lazy val pewsim = "com.workflowfm" %% "pew-simulator" % "1.5.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.workflowfm",
      scalaVersion := "2.12.12",
      version      := "1.0-SNAPSHOT"
    )),
    name := """HealthcareHandoverStateful""",
    libraryDependencies += pew,
    libraryDependencies += sim,
    libraryDependencies += pewsim,
	scalaSource in Compile := baseDirectory.value / "src",
    resourceDirectory in Compile := baseDirectory.value / "resources"
  )


