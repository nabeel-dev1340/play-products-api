lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := "products-api",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      guice,
      javaJpa,
      "com.h2database" % "h2" % "1.4.200",
      "org.hibernate" % "hibernate-core" % "5.6.15.Final",
      "io.dropwizard.metrics" % "metrics-core" % "4.2.17",
      "com.palominolabs.http" % "url-builder" % "1.1.5",
      "net.jodah" % "failsafe" % "2.4.4",
    ),
    PlayKeys.externalizeResources := false,
    (Test / testOptions) := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v")),
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    )
  )
