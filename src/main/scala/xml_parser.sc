import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import scala.xml._


//val dom = HTMLParser.fromUrl("https://www.google.com/finance?q=gdxj")
//
//val table = (dom \ "body" \\ "div").filter(_ \@ "class" == "snap-panel-and-plusone") \\ "table" filter(_ \@ "class" == "snap-data")
//
//((table \\ "tr")(2) \ "td")(1).text.toString.toDouble;


class TParser() extends JavaTokenParsers with RegexParsers {
    def attrValue = "[a-zA-Z-_]+".r

    def attr: Parser[Attr] = "@" ~ "class" ~ ":" ~ attrValue ^^ {
        case at ~ cname ~ c ~ value => Attr(cname, value)
    }

    def idx = "[" ~> "[0-9]+".r <~ "]" ^^ {
        case idx => idx.toInt
    }

    def tags = "div" | "span" | "table" | "tr" | "td"

    def strT = "str" ^^ { case x => StrTarget}

    def intT = "int" ^^ { case x => IntTarget}

    def decimalT = "decimal" ^^ { case x => DecimalTarget }

    def directTag = "." ~> tag

    def indirectTag = ".." ~> tag

    def valueT = "!" ~> (decimalT | intT | strT)

    def tag: Parser[TagTarget] = (tags ~ opt(attr) ~ opt(idx)) ~ (directTag | indirectTag | valueT) ^^ {
        case tname ~ attr ~ idx ~ value => TagTarget(tname, value, attr, idx)
    }

    def apply(dsl: String) = parseAll(tag, dsl)
}

val s = "div@class:snap-panel-and-plusone.div@class:snap-panel.table@class:span-data.tr[2].td[1]!decimal"
val tp = new TParser()
tp(s)
//tp.attr(new tp.lexical.Scanner("class:\"attr-value\""))

