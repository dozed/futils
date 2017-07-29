package futils

import scalaz._, Scalaz._

object metrics {

  // print duration of the evaluation of an expression
  def logTime[T](f: => T): T = {
    val (d, res) = time(f)
    println(s"took $d ms")
    res
  }

  // evaluate an expression, return duration and result
  def time[T](f: => T): (Long, T) = {
    val start = System.currentTimeMillis
    val res = f
    val delta = System.currentTimeMillis - start
    (delta, res)
  }

}
