import java.io.IOException
import java.nio.file.{CopyOption, Files}
import java.nio.file.StandardCopyOption._
name := "OpenCC-Java"

version := "0.0.1"

scalaVersion := "2.11.4"

autoScalaLibrary := false

libraryDependencies ++= Seq(
  "net.java.dev.jna" % "jna" % "4.1.0"
)

javacOptions ++= Seq("-encoding", "UTF-8")

crossPaths := false

lazy val copyLibTask = TaskKey[Unit]("copy","Copy libraries to out")

copyLibTask:={
  println("Copying libraries to out")
  val sourceLib = (artifactPath in (Compile, packageBin)).value
  val out = baseDirectory.value / "out"
  if(!out.exists()) Files.createDirectory(out.toPath)
  if(!out.isDirectory) throw new IOException("Exist file named out, either rename it or stop packaging.")
  val targetLib = out / sourceLib.name
  Files.copy(sourceLib.toPath,targetLib.toPath,REPLACE_EXISTING)
  println("done")
}

copyLibTask <<= copyLibTask triggeredBy (packageBin in Compile)
