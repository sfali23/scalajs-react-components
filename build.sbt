inThisBuild(
  List(
    scalaVersion := Dependencies.Versions.scala,
    organization := "com.dbrsn.scalajs.react.components",
    scalacOptions := Seq(
      "-deprecation", // warning and location for usages of deprecated APIs
      "-encoding",
      "UTF-8",
      "-feature", // warning and location for usages of features that should be imported explicitly
      "-unchecked", // additional warnings where generated code depends on assumptions
      "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
      "-Xfuture", // Turn on future language features.
      "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
      "-Ypartial-unification", // Enable partial unification in type constructor inference
      "-Ywarn-dead-code", // Warn when dead code is identified.
      "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
      "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
      "-Ywarn-numeric-widen", // Warn when numerics are widened.
      "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
      "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
      "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
      "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
      "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
      "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
      "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
      "-Xlint:option-implicit", // Option.apply used implicit view.
      "-Xlint:package-object-classes", // Class or object defined in package object.
      "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
      "-Xlint:unsound-match", // Pattern match may not be typesafe.
      "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
      "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
      "-Ywarn-unused:locals", // Warn if a local definition is unused.
      "-Ywarn-unused:params", // Warn if a value parameter is unused.
      "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
      "-Ywarn-unused:privates" // Warn if a private member is unused.
    ),
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true
  ))

lazy val macros = project
  .in(file("macros"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.`scalajs-react-core`.value,
      Dependencies.`scalajs-react-extra`.value,
      Dependencies.scalatest.value % Test
    )
  )

lazy val `generator-semantic-ui` = project
  .in(file("generator-semantic-ui"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    version in webpack := "2.6.1",
    libraryDependencies ++= Seq(
      Dependencies.`ammonite-ops`,
      Dependencies.scalatest.value % Test
    )
  )
  .settings(
    useYarn := true,
    npmDependencies in Compile := Seq(
      Dependencies.`semantic-ui-react`,
      Dependencies.react,
      Dependencies.`react-dom`
    )
  )
  .settings(
    scalacOptions := Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-language:implicitConversions", // Allow definition of implicit functions called views
      "-language:postfixOps",
      "-P:scalajs:sjsDefinedByDefault"
    )
  )
  .settings(
    publishArtifact := false
  )

lazy val generateSui = TaskKey[Seq[File]]("generateSui")

lazy val `semantic-ui` = project
  .in(file("semantic-ui"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(macros)
  .settings(
    generateSui := {
      val genDir = sourceManaged.value
      genDir.mkdirs()
      val res = runner.value.run(
        "com.olvind.sui.SuiRunner",
        (fullClasspath in (`generator-semantic-ui`, Runtime)).value.files,
        List(
          (npmUpdate in (`generator-semantic-ui`, Compile)).value / "node_modules" / "semantic-ui-react" / "dist" / "commonjs",
          sourceManaged.value / "main"
        ) map (_.absolutePath),
        streams.value.log
      )
      val pathFinder: PathFinder = sourceManaged.value ** "*.scala"
      pathFinder.get.filter(_.getAbsolutePath.contains("semanticui"))
    },
    sourceGenerators in Compile += generateSui
  )
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.`scalajs-react-test`.value % Test,
      Dependencies.specs2.value               % Test
    )
  )