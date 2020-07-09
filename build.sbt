lazy val pew = "com.workflowfm" %% "pew" % "1.4.0-SNAPSHOT"
lazy val sim = "com.workflowfm" %% "wfm-simulator" % "0.2.1"
lazy val pewsim = "com.workflowfm" %% "pew-simulator" % "1.4.0-SNAPSHOT"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.workflowfm",
      scalaVersion := "2.12.6",
      version      := "1.0-SNAPSHOT"
    )),
    name := """HealthcareHandoverStateful""",
    libraryDependencies += pew,
    libraryDependencies += sim,
    libraryDependencies += pewsim,
    libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT",
	scalaSource in Compile := baseDirectory.value / "src",
    resourceDirectory in Compile := baseDirectory.value / "resources"
  )


