scalaVersion := "2.12.6"
name := "hello-world"
organization := "com.github.sebarys"
version := "0.1.0"

val catsVersion = "1.1.0"

// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:
libraryDependencies += "org.typelevel" %% "cats-core" % "1.1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,

  // Test
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification", // makes e.g. from Function1[-A, +B] --> type F[A] = A => B
  "-language:higherKinds",
  "-explaintypes", // Explain type errors in more detail.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
)