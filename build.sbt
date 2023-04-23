lazy val generateCode = taskKey[Unit]("Generate benchmark code")
lazy val benchmark    = inputKey[Unit]("Run the benchmarks")

lazy val commonSettings = Seq(
  version      := "0.1.0",
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
    name                                   := "circe-derivation",
    scalacOptions ++= Seq("-Xmax-inlines", "128"),
    libraryDependencies += "io.circe"      %% "circe-core"             % "0.14.3",
    libraryDependencies += "io.circe"      %% "circe-generic"          % "0.14.3",
    libraryDependencies += "net.katsstuff" %% "perspective-derivation" % "0.2.0-SNAPSHOT",
    libraryDependencies += "org.typelevel" %% "shapeless3-deriving"    % "3.0.1",
    generateCode := {
      GenerateCirceSources.generateRuntimeFiles(
        (Compile / scalaSource).value.toPath,
        Seq("perspective", "circederivation"),
        GenerateCirceSources.circeDerivationCaseClasses,
        isScala3 = true
      )
    },
    benchmark := (Jmh / run).evaluated
  )

lazy val circePerspectiveDerivationScala2 = project
  .in(file("scala2/circe-derivation"))
  .enablePlugins(JmhPlugin)
  .settings(
    commonScala2Settings,
    name                                   := "circe-derivation",
    libraryDependencies += "io.circe"      %% "circe-core"                   % "0.14.3",
    libraryDependencies += "io.circe"      %% "circe-generic"                % "0.14.3",
    libraryDependencies += "io.circe"      %% "circe-derivation"             % "0.13.0-M5",
    libraryDependencies += "net.katsstuff" %% "perspectivescala2-derivation" % "0.2.0-SNAPSHOT",
    libraryDependencySchemes += "io.circe" %% "circe-core"                   % "always",
    libraryDependencySchemes += "io.circe" %% "circe-derivation"             % "always",
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    generateCode := {
      GenerateCirceSources.generateRuntimeFiles(
        (Compile / scalaSource).value.toPath,
        Seq("perspective", "circederivation"),
        GenerateCirceSources.circeDerivationCaseClasses,
        isScala3 = false
      )
    },
    benchmark := (Jmh / run).evaluated
  )

import GenerateCirceSources.CompiletimeBenchmark

lazy val circePerspectiveDerivationCompiletimeScala3 =
  project
    .in(file("dotty/circe-derivation-compiletime"))
    .enablePlugins(JmhPlugin)
    .dependsOn(shapeless2)
    .settings(
      commonScala3Settings,
      name                                    := "circe-derivation-compiletime",
      libraryDependencies += "org.scala-lang" %% "scala3-compiler" % scalaVersion.value,
      libraryDependencies ++= Seq(
        "io.circe"      %% "circe-core"             % "0.14.3",
        "io.circe"      %% "circe-generic"          % "0.14.3",
        "net.katsstuff" %% "perspective-derivation" % "0.2.0-SNAPSHOT",
        "org.typelevel" %% "shapeless3-deriving"    % "3.0.1"
      ).flatMap(m => List(m, m % CompiletimeBenchmark)),
      CompiletimeBenchmark / managedClasspath := GenerateCirceSources.resolveCompiletimeBenchmarkClasspath.value ++ (Compile / internalDependencyAsJars).value,
      generateCode := {
        GenerateCirceSources.generateCompiletimeFiles(
          (Compile / resourceDirectory).value.toPath,
          Seq("perspective", "circederivationcompiletime"),
          GenerateCirceSources.circeDerivationCaseClasses,
          isScala3 = true,
          (CompiletimeBenchmark / managedClasspath).value.files,
          Seq(
            "PerspectiveDerive.scala",
            "PerspectiveFasterDerive.scala",
            "PerspectiveInlineDerive.scala",
            "PerspectiveUnrollingDerive.scala",
            "Shapeless2Derive.scala",
            "Shapeless3Derive.scala"
          ),
          compilers.value
        )
      },
      ivyConfigurations += CompiletimeBenchmark,
      benchmark := Def.inputTaskDyn {
        val classPathTypes = (CompiletimeBenchmark / classpathTypes).value
        val cp = (CompiletimeBenchmark / managedClasspath).value
        val args = Def.spaceDelimited().parsed.mkString(" ")

        val params = GenerateCirceSources.jmhCompiletimeParams(
          (Compile / resourceDirectory).value.toPath,
          Seq("perspective", "circederivationcompiletime"),
          isScala3 = true,
          cp.files
        )

        Def.task {
          (Jmh / run).toTask(s" $params $args ").value
        }
      }.evaluated
    )

lazy val circePerspectiveDerivationCompiletimeScala2 =
  project
    .in(file("scala2/circe-derivation-compiletime"))
    .enablePlugins(JmhPlugin)
    .settings(
      commonScala2Settings,
      name                                   := "circe-derivation-compiletime",
      libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      libraryDependencies ++= Seq(
        "io.circe"      %% "circe-core"                   % "0.14.3",
        "io.circe"      %% "circe-generic"                % "0.14.3",
        "io.circe"      %% "circe-derivation"             % "0.13.0-M5",
        "net.katsstuff" %% "perspectivescala2-derivation" % "0.2.0-SNAPSHOT"
      ).flatMap(m => List(m, m % CompiletimeBenchmark)),
      libraryDependencySchemes ++= Seq(
        "io.circe" %% "circe-core"       % "always",
        "io.circe" %% "circe-derivation" % "always"
      ).flatMap(m => List(m, m % CompiletimeBenchmark)),
      CompiletimeBenchmark / managedClasspath := GenerateCirceSources.resolveCompiletimeBenchmarkClasspath.value,
      generateCode := {
        GenerateCirceSources.generateCompiletimeFiles(
          (Compile / resourceDirectory).value.toPath,
          Seq("perspective", "circederivationcompiletime"),
          GenerateCirceSources.circeDerivationCaseClasses,
          isScala3 = false,
          (CompiletimeBenchmark / managedClasspath).value.files,
          Seq(
            "PerspectiveDerive.scala",
            "PerspectiveFasterDerive.scala",
            "Shapeless2Derive.scala"
          ),
          compilers.value
        )
      },
      ivyConfigurations += CompiletimeBenchmark,
      benchmark := Def.inputTaskDyn {
        val classPathTypes = (CompiletimeBenchmark / classpathTypes).value
        val cp             = (CompiletimeBenchmark / managedClasspath).value
        val args           = Def.spaceDelimited().parsed.mkString(" ")

        val params = GenerateCirceSources.jmhCompiletimeParams(
          (Compile / resourceDirectory).value.toPath,
          Seq("perspective", "circederivationcompiletime"),
          isScala3 = false,
          cp.files
        )

        Def.task {
          (Jmh / run).toTask(s" $params $args ").value
        }
      }.evaluated
    )

lazy val perspectivePerformanceRoot = project
  .in(file("."))
  .aggregate(
    circePerspectiveDerivationScala3,
    circePerspectiveDerivationScala2,
    circePerspectiveDerivationCompiletimeScala3,
    circePerspectiveDerivationCompiletimeScala2
  )
  .settings(
    commonSettings
  )
