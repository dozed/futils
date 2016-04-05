package futils

import scala.xml.MetaData
import java.io.StringReader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.xml.sax.InputSource

import scala.xml.parsing.NoBindingFactoryAdapter

import scalaz._, Scalaz._

object xml {

  object TagSoup {

    lazy val parserFactory = new SAXFactoryImpl
    lazy val adapter = new NoBindingFactoryAdapter

    def apply(s: String): scala.xml.Node = {
      adapter.loadXML(new InputSource(new StringReader(s)), parserFactory.newSAXParser)
    }


  }


  implicit class MetaDataExt(md: MetaData) {

    def hasAttributeWithValue(key: String, value: String): Boolean = md.asAttrMap.get(key).contains(value)

    def hasAttribute(key: String): Boolean = md.asAttrMap.get(key).isDefined

  }


  // https://github.com/chrsan/css-selectors-scala/blob/master/src/main/scala/se/fishtank/css/selectors/Selectors.scala
  // https://github.com/philcali/css-query/blob/master/core/src/main/scala/Query.scala

  implicit class NodeExt(n: scala.xml.Node) {

    def withClass(value: String): scala.xml.NodeSeq = n.filter(_.hasClass(value))

    def hasClass(value: String): Boolean = attributeValue("class").map(_.split(" ")).exists(_.contains(value))

    def hasId(value: String): Boolean = hasAttributeWithValue("id", value)

    def hasAttributeWithValue(key: String, value: String): Boolean = n.attributes.asAttrMap.get(key).contains(value)

    def hasAttribute(key: String): Boolean = n.attributes.asAttrMap.get(key).isDefined

    def attributeValue(key: String): Option[String] = n.attributes.asAttrMap.get(key).headOption

    def firstText: scala.xml.Node = n.descendant.head

  }


  implicit val nodeShows = Show.shows[scala.xml.Node](_.text.trim)

}
