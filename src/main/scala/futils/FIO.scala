package futils

object FIO {

  import scala.sys.process._

  def ls(dir: String): List[String] = (s"ls $dir -1"!!).split("\n").map(x => s"$dir/$x").toList

  def readLines(f: String): List[String] = scala.io.Source.fromFile(f).getLines.toList

  def readFile(f: String): String = readLines(f).mkString("\n")

  def writeFile(f: String, content: String): Unit = {
    val pw = new java.io.PrintWriter(f)
    pw.println(content)
    pw.close
  }

}
