package futils

// source: https://gist.github.com/shajra/6315283
object taskC {

  import scala.concurrent.{ExecutionContext, Promise, Future => SFuture}
  import scala.util.Try
  import scalaz.\/
  import scalaz.concurrent.{Task => ZTask}

  //  def toTask[T](ft: => SFuture[T]): ZTask[T] = {
  //    import scalaz._
  //    ZTask.async { register =>
  //      ft.onComplete({
  //        case scala.util.Success(v) => register(\/-(v))
  //        case scala.util.Failure(ex) => register(-\/(ex))
  //      })
  //    }
  //  }

  def fromScala[A]
  (future: SFuture[A])(implicit ec: ExecutionContext): ZTask[A] =
    ZTask async (handlerConversion andThen future.onComplete)

  def fromScalaDeferred[A]
  (future: => SFuture[A])(implicit ec: ExecutionContext): ZTask[A] =
    ZTask delay fromScala(future)(ec) flatMap identity

  def unsafeToScala[A](task: ZTask[A]): SFuture[A] = {
    val p = Promise[A]
    task runAsync {
      _ fold(p failure _, p success _)
    }
    p.future
  }

  private def handlerConversion[A]: ((Throwable \/ A) => Unit) => Try[A] => Unit =
    callback => { t: Try[A] => \/.fromTryCatchNonFatal(t.get) } andThen callback

}
