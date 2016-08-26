package futils

import scalaz._, Scalaz._

/**
  * Created by stefan on 8/26/16.
  */
object threadLocals {


  trait ThreadLocalValue[A] {
    def get: A
  }

  object ThreadLocalValue {

    def instance[A](init: () => A): ThreadLocalValue[A] = {
      val store = new ThreadLocal[A]()

      new ThreadLocalValue[A] {

        def get: A = {
          val a = store.get()
          if (a == null) {
            val a = init()
            store.set(a)
            a
          } else {
            a
          }
        }
      }
    }

  }


}
