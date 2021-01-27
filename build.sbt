lazy val pew = "com.workflowfm" %% "pew" % "1.4.0+43-59c462d0+20210127-1422"
lazy val sim = "com.workflowfm" %% "proter" % "0.6-beta+0-73730c27+20210127-1732"
lazy val pewsim = "com.workflowfm" %% "pew-simulator" % "1.4.0+43-59c462d0+20210127-1422"

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
//compil    libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "1.0.0",
	scalaSource in Compile := baseDirectory.value / "src",
    resourceDirectory in Compile := baseDirectory.value / "resources"
  )


