package futils

import rl.UrlCodingUtils

import scalaz._
import Scalaz._
import scala.collection.immutable.BitSet
import scala.util.Try

object UrlEncoding {

  def formEncodedStringToMap(s: String): Map[String, String] = Try {
    s.split("&").toList flatMap { s =>
      s.split("=").toList match {
        case key :: value :: Nil => Some(UrlCodingUtils.urlDecode(key) -> UrlCodingUtils.urlDecode(value))
        case _ => None
      }
    } toMap
  } getOrElse Map[String, String]()

  val toSkip = BitSet((('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') ++ "!$'()*+,;:/?@-._~".toSet).map(_.toInt): _*)
  def encode(s: String) = UrlCodingUtils.urlEncode(s, toSkip = toSkip)
  def encodeMap(s: Map[String, String]) = s map { case (key, value) => f"${encode(key)}=${encode(value)}" } mkString "&"

}
