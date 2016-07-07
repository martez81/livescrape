/**
  *
  * Resources:
  * http://www.hars.de/2009/01/html-as-xml-in-scala.html
  */

import org.xml.sax.InputSource
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import scala.xml._
import parsing._
import scala.util.parsing.combinator.{RegexParsers, JavaTokenParsers, lexical}


//class HTML5Parser extends NoBindingFactoryAdapter {
//    override def loadXML(source : InputSource, _p: SAXParser) = {
//        loadXML(source)
//    }
//
//    def loadXML(source : InputSource) = {
//        import nu.validator.htmlparser.{sax,common}
//        import sax.HtmlParser
//        import common.XmlViolationPolicy
//
//        val reader = new HtmlParser
//        reader.setXmlPolicy(XmlViolationPolicy.ALLOW)
//        reader.setContentHandler(this)
//        reader.parse(source)
//        rootElem
//    }
//}

trait HTMLFactoryAdapter extends FactoryAdapter {
    val emptyElements = Set("area", "object", "iframe", "base", "br", "col", "hr", "img", "input", "link", "meta", "param")
    def nodeContainsText(localName: String) = !(emptyElements contains localName)
}

class HTMLParser() {
    val parserFactory = new SAXFactoryImpl()
    val parser = parserFactory.newSAXParser()

    def fromUrl(src: String): scala.xml.Node = {
        val source = new org.xml.sax.InputSource(src)
        val adapter = new scala.xml.parsing.NoBindingFactoryAdapter
        adapter.loadXML(source, parser)
    }
}

/**
  * div@class:snap-panel-and-plusone.div@class:snap-panel.table@class:span-data.tr[2].td[1]!decimal
  */
class TargetParser() extends JavaTokenParsers with RegexParsers {
    def attrValue = "[a-zA-Z0-9_-]+".r

    def attr: Parser[Attr] = "@" ~ ("class" | "id" | "data-gameid") ~ ":" ~ attrValue ^^ {
        case at ~ cname ~ c ~ value => Attr(cname, value)
    }

    def idx = "[" ~> "[0-9]+".r <~ "]" ^^ { case idx => idx.toInt }

    def tags = "div" | "span" | "table" | "tr" | "td" | "p"

    def rawT = "raw" ^^ { case x => RawTarget}

    def strT = "str" ^^ { case x => StrTarget}

    def intT = "int" ^^ { case x => IntTarget}

    def decimalT = "decimal" ^^ { case x => DecimalTarget }

    def childTag = "." ~> tag

    def valueT = "!" ~> (decimalT | intT | strT | rawT)

    // TODO: Parser creates node with td and target value where tval should be on its own
    def tag: Parser[Tag] = (tags ~ opt(attr) ~ opt(idx)) ~ opt(childTag) ~ opt(valueT) ^^ {
        case tname ~ attr ~ idx ~ child ~ value => Tag(tname, child, attr, value, idx)
    }

    def apply(dsl: String) = parseAll(tag, dsl)
}

object Resolver {
    def apply(t: Tag, dom: Seq[Node]): List[Any] = {
        t match {
            case Tag(_, Some(ch), Some(attr), None, Some(idx)) => {
                apply(ch, (dom \\ t.name).filter(_ \@ attr.key == attr.value)(idx) :: Nil)
            }
            case Tag(_, Some(ch), Some(attr), None, None) => {
                apply(ch, (dom \\ t.name).filter(_ \@ attr.key == attr.value))
            }
            case Tag(_, Some(ch), None, None, Some(idx)) => {
                apply(ch, (dom \\ t.name) (idx) :: Nil)
            }
            case Tag(_, Some(ch), None, None, None) => {
                apply(ch, (dom \\ t.name))
            }
            case Tag(_, None, _, Some(value), _) => {
                val raw: List[String] = t match {
                    case Tag(_, None, Some(attr), _, Some(idx)) =>
                        List((dom \\ t.name).filter(_ \@ attr.key == attr.value)(idx).text)
                    case Tag(_, None, Some(attr), _, None) =>
                        (dom \\ t.name).filter(_ \@ attr.key == attr.value).map(_.text).toList
                    case Tag(_, None, None, _, Some(idx)) =>
                        List((dom \\ t.name)(idx).text)
                    case Tag(_, None, None, _, None) =>
                        (dom \\ t.name).map(_.text).toList
                    case _ => throw new Exception("Resolver Error")
                }

                // TODO: deal with missing data, use Option
                value match {
                    case IntTarget => raw.map(x => "([0-9]+)".r.findFirstMatchIn(x).get.toString.toInt)
                    case DecimalTarget => raw.map(x => "([0-9.]+)".r.findFirstMatchIn(x).get.toString.toDouble)
                    case StrTarget => raw.map(x => "[a-zA-Z0-9-_.,^|]+".r.findFirstMatchIn(x).get.toString)
                    case RawTarget => raw.map(x => x.toString)
                    case _ => throw new Exception("Final value parsing error.")
                }
            }
        }
    }
}

//object Resolve {
//    def apply(tloc: Tag, dom: Seq[Node]): Any = {
//        tloc match {
//            case t: Tag => t.attr match {
//                case Some(a) => {
//                    if (t.childIdx.isInstanceOf[Some[Int]]) {
//                        Resolve(t.child.get, (dom \ t.name).filter(_ \@ a.key == a.value)(t.childIdx.get) :: Nil)
//                    } else {
//                        Resolve(t.child.get, (dom \ t.name).filter(_ \@ a.key == a.value))
//                    }
//                }
//                case _ => {
//                    if (t.childIdx.isInstanceOf[Some[Int]]) {
//                        Resolve(t.child.get, (dom \ t.name)(t.childIdx.get) :: Nil)
//                    } else {
//                        Resolve(t.child.get, dom \ t.name)
//                    }
//                }
//            }
//            case IntTarget => ("([0-9]+)".r).findFirstMatchIn(dom.text).get.toString.toInt
//            case DecimalTarget => ("([0-9.]+)".r).findFirstMatchIn(dom.text).get.toString.toDouble
//            case StrTarget => ("[a-zA-Z0-9-_.,]+".r).findFirstMatchIn(dom.text).get.toString
//            case RawTarget => dom.text
//            case _ => throw new Exception("Final value parsing error.")
//        }
//    }
//}
