name := "ReflectivePhrases"
version := "0.1.0"
scalaVersion := "2.11.7"
mainClass in assembly := Some("net.andrewresearch.reflectivephrases.Application")

val finchVersion = "0.9.1"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % finchVersion,
  "com.github.finagle" %% "finch-json4s" % finchVersion,
  "com.twitter" %% "twitter-server" % "latest.integration",
  "io.circe" %% "circe-core" % "0.2.0",
  "io.circe" %% "circe-generic" % "0.2.0",
  "io.circe" %% "circe-parse" % "0.2.0",
  "cc.factorie" %% "factorie" % "1.1.1",
  "cc.factorie.app.nlp" % "all-models" % "1.0.0",
  "com.typesafe" % "config" % "latest.integration",
  "org.slf4j" % "jcl-over-slf4j" % "latest.integration",
  "ch.qos.logback" % "logback-classic" % "latest.integration"
).map(_.exclude("commons-logging", "commons-logging"))

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "twttr" at "https://maven.twttr.com/"
resolvers += "factorie" at "http://dev-iesl.cs.umass.edu/nexus/content/groups/public"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheUnzip = false)
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }