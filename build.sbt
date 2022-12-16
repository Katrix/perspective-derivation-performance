lazy val generateCode = taskKey[Unit]("Generate benchmark code")

lazy val commonSettings = Seq(
  version := "0.1.0",
  organization := "net.katsstuff"
)

lazy val commonScala2Settings = commonSettings ++ Seq(
  scalaVersion := "2.13.9",
  moduleName := {
    val old = moduleName.value
    if (old == "perspective") "perspectivescala2"
    else s"perspectivescala2-$old"
  },
  addCompilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.2").cross(CrossVersion.full)),
  scalacOptions += "-explaintypes"
)

lazy val commonScala3Settings = commonSettings ++ Seq(
  scalaVersion := "3.2.0",
  moduleName := {
    val old = moduleName.value
    if (old == "perspective") old
    else s"perspective-$old"
  },
  scalacOptions += "-Ykind-projector"
)

lazy val shapeless2 = project.settings(
  commonScala3Settings,
  name := "shapeless2"
)

lazy val circePerspectiveDerivationScala3 = project
  .in(file("dotty/circe-derivation"))
  .enablePlugins(JmhPlugin)
  .dependsOn(shapeless2)
  .settings(
    commonScala3Settings,
    name := "circe-derivation",
    libraryDependencies += "io.circe"      %% "circe-core"             % "0.14.3",
    libraryDependencies += "io.circe"      %% "circe-generic"          % "0.14.3",
    libraryDependencies += "net.katsstuff" %% "perspective-derivation" % "0.2.0-SNAPSHOT",
    libraryDependencies += "org.typelevel" %% "shapeless3-deriving"    % "3.0.1",
    generateCode := {
      GenerateCirceSources.generateRuntimeFiles(
        ((Compile / scalaSource).value / "perspective" / "circederivation").toPath,
        GenerateCirceSources.circeDerivationCaseClasses,
        isScala3 = true,
        "perspective.circederivation"
      )
    }
  )

lazy val circePerspectiveDerivationScala2 = project
  .in(file("scala2/circe-derivation"))
  .enablePlugins(JmhPlugin)
  .settings(
    commonScala2Settings,
    name := "circe-derivation",
    libraryDependencies += "io.circe"      %% "circe-core"                   % "0.14.3",
    libraryDependencies += "io.circe"      %% "circe-generic"                % "0.14.3",
    libraryDependencies += "io.circe"      %% "circe-derivation"             % "0.13.0-M5",
    libraryDependencies += "net.katsstuff" %% "perspectivescala2-derivation" % "0.2.0-SNAPSHOT",
    libraryDependencySchemes += "io.circe" %% "circe-core"                   % "always",
    libraryDependencySchemes += "io.circe" %% "circe-derivation"             % "always",
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    generateCode := {
      GenerateCirceSources.generateRuntimeFiles(
        ((Compile / scalaSource).value / "perspective" / "circederivation").toPath,
        GenerateCirceSources.circeDerivationCaseClasses,
        isScala3 = false,
        "perspective.circederivation"
      )
    }
  )

lazy val perspectivePerformanceRoot = project
  .in(file("."))
  .aggregate(
    circePerspectiveDerivationScala3,
    circePerspectiveDerivationScala2
  )
  .settings(
    commonSettings
  )
