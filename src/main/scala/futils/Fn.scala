package futils

import scalaz._
import Scalaz._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Fn {

  def delayed[A, B](d: Long)(f: A => B): A => B  = { a =>
    val b = f(a)
    Thread.sleep(d)
    b
  }

  def withLog[A, B](log: A => String)(f: A => B): A => B =
    { (a: A) => println(log(a)); a } andThen f

  def await[A](f: => Future[A]): A = {
    Await.result(f, 5 minutes)
  }

}
