package futils

import java.io.File

import org.http4s._
import org.http4s.dsl._
import org.http4s.headers.`Content-Type`

import org.json4s.JValue
import org.json4s.ext.scalaz.JsonScalaz._
import org.json4s.jackson._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream.Process
import _root_.scalaz.stream.io._

import _root_.scalaz._, Scalaz._

object http4su {


  implicit class DecodeResultExt[T](r: DecodeResult[T]) {

    def require: T = r.run.unsafePerformSync.getOrElse(sys.error("DecodeResult.require"))

  }

  implicit class ResponseExt(res: Response) {

    def cookies: List[Cookie] = res.headers.toList.collect {
      case headers.`Set-Cookie`(h) => h.cookie
    }

    def cookie(name: String): Option[Cookie] = cookies.find(_.name === name)

    def cookieAs[A](name: String)(implicit ev: Cookie => Option[A]) = {
      cookie(name).flatMap(ev)
    }

    def location: Option[Uri] = res.headers.toList.collectFirst {
      case headers.`Location`(h) => h.uri
    }

  }

  implicit class RequestExt(req: Request) {

    def withCookie(cookie: Cookie) = {
      req.copy(headers = req.headers.put(headers.Cookie(cookie)))
    }

  }


  // QueryParamDecoder is for single query parameters
  //  def listDecoder[A:QueryParamDecoder](implicit decodeA: QueryParamDecoder[A]): QueryParamDecoder[List[A]] = new QueryParamDecoder[List[A]] {
  //    override def decode(value: QueryParameterValue): ValidationNel[ParseFailure, List[A]] = ???
  //  }

  abstract class ListQueryParamDecoderMatcher[A:QueryParamDecoder](name: String) {
    def unapply(params: Map[String, Seq[String]]): Option[List[A]] =
      params.get(name).map(values =>
        values.toList.flatMap(s =>
          QueryParamDecoder[A].decode(QueryParameterValue(s)).toOption
        )
      ) orElse Some(List.empty[A])
  }

  abstract class OptionalDefaultQueryParamDecoderMatcher[A:QueryParamDecoder](name: String, default: A) {
    def unapply(params: Map[String, Seq[String]]): Option[A] =
      params.get(name).map(values =>
        values.toList.flatMap(s =>
          QueryParamDecoder[A].decode(QueryParameterValue(s)).toOption
        ).headOption | default
      )
  }


  def queryParamDecoderFromJsonr[A:JSONR]: QueryParamDecoder[A] = new QueryParamDecoder[A]{
    override def decode(value: QueryParameterValue): ValidationNel[ParseFailure, A] = {
      value.value.validate[A].leftMap(_ => ParseFailure("bad request", s"could not decode value. $value").wrapNel)
    }
  }

  implicit def jsonwAsEntityEncoder[A:JSONW]: EntityEncoder[A] = {

    EntityEncoder
      .stringEncoder(Charset.`UTF-8`)
      .contramap { r: A => compactJson(implicitly[JSONW[A]].write(r)) }
      .withContentType(`Content-Type`(MediaType.`application/json`, Charset.`UTF-8`))

  }

  implicit def jsonrAsEntityDecoder[A:JSONR]: EntityDecoder[A] = {

    jsonDecoder.flatMapR(json => implicitly[JSONR[A]].read(json).disjunction.fold(
      _ => org.http4s.DecodeResult.failure[A](MalformedMessageBodyFailure("bad request")),
      a => org.http4s.DecodeResult.success[A](a)
    ))

  }

  implicit val jsonDecoder: EntityDecoder[JValue] = {
    EntityDecoder.text(Charset.`UTF-8`)
      .flatMapR { s: String => parseJsonOpt(s).fold(
        org.http4s.DecodeResult.failure[JValue](MalformedMessageBodyFailure("bad request"))
      )(
        a => org.http4s.DecodeResult.success[JValue](a)
      )}
  }


  def staticResource(f: File): Task[Response] = {

    if (f.exists) {

      if (f.isDirectory) {

        val listing = <ul>
          {f.listFiles() map { x =>
            <li>
              <a href={f.getName + "/" + x.getName}>
                {x.getName}
              </a>
            </li>
          }}
        </ul>

        val bytes = Task[Array[Byte]](listing.toString.getBytes)

        val mime = {
          val parts = f.getName.split('.')
          if (parts.length > 0) MediaType.forExtension(parts.last)
            .getOrElse(MediaType.`application/octet-stream`)
          else MediaType.`application/octet-stream`
        }

        Ok(bytes).putHeaders(`Content-Type`(MediaType.`text/html`))

      } else {

        val bytes = Process.constant(8*1024)
          .toSource
          .through(chunkR(new java.io.FileInputStream(f)))
          .runLog
          .run
          .map(_.toArray)
          .toArray
          .flatten

        val mime = {
          val parts = f.getName.split('.')
          if (parts.length > 0) MediaType.forExtension(parts.last)
            .getOrElse(MediaType.`application/octet-stream`)
          else MediaType.`application/octet-stream`
        }

        Ok(bytes).putHeaders(`Content-Type`(mime))

      }

    } else NotFound()

  }

}

