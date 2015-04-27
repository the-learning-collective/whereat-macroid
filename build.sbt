import android.Dependencies.aar
import android.Keys._

//MACROID STUFF//

android.Plugin.androidBuild

platformTarget in Android := "android-21"

name := "whereat"

scalaVersion := "2.11.6"

run <<= run in Android

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "jcenter" at "http://jcenter.bintray.com",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

scalacOptions in (Compile, compile) ++=
  (dependencyClasspath in Compile).value.files.map("-P:wartremover:cp:" + _.toURI.toURL)

scalacOptions in (Compile, compile) ++= Seq(
  "-P:wartremover:traverser:macroid.warts.CheckUi"
)


scalacOptions ++= Seq("-feature", "-deprecation", "-target:jvm-1.7")

scalacOptions in Test ++= Seq("-Yrangepos")

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

libraryDependencies ++= Seq(
  aar("org.macroid" %% "macroid" % "2.0.0-M4"),
  aar("org.macroid" %% "macroid-viewable" % "2.0.0-M4"),
  aar("com.android.support" % "support-v4" % "21.0.3"),
  "com.google.android.gms" % "play-services" % "6.5.87",
  "io.taig" %% "communicator" % "2.0.1",
  "com.typesafe.play" %% "play-json" % "2.3.4",
  "org.specs2" %% "specs2-core" % "3.5" % "test",
  compilerPlugin("org.brianmckenna" %% "wartremover" % "0.10")
)

proguardScala in Android := true

proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic"
)

apkbuildExcludes in Android ++= Seq (
  "META-INF/LICENSE",
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE",
  "META-INF/NOTICE.txt"
)
