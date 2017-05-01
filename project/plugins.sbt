libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

addSbtPlugin("io.get-coursier"   %% "sbt-coursier" % "1.0.0-RC1")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"  % "0.6.3")
addSbtPlugin("com.timushev.sbt"  % "sbt-updates"   % "0.3.0")
addSbtPlugin("com.github.gseitz" % "sbt-release"   % "1.0.4")
addSbtPlugin("me.lessis"         % "bintray-sbt"   % "0.3.0")
