
import java.net.{URLConnection, URL}
import scala.xml
import scala.io
import scala.util.matching.Regex

//val reg = """<([A-Z][A-Z0-9]*)\b[^>]*>(.*?)</\1>""".r
//reg.findFirstMatchIn("<div>hello</div>")
//val dom = xml.XML.loadString("<div><p id=\"price\">14.10</p></div>")
val dom = xml.XML.loadFile("/Users/marcin/projects/scala/livescrape/src/main/resources/test.html")


// Example of Google Finance GDXJ page
val closePrice = TagTarget(
    "span",
    Some(
        Attr("id", "ref_5768664_l")
    ),
    Some(DecimalTarget)
)

val openPrice = TagTarget(
    "div",
    Some(Attr("class", "snap-panel-and-plusone")),
    Some(
        TagTarget(
            "div",
            Some(Attr("class", "snap-panel")),
            Some(
                TagTarget(
                    "table",
                    Some(Attr("class", "span-data")),
                    Some(
                        TagTarget(
                            "tr",
                            None,
                            Some(
                                TagTarget(
                                    "td",
                                    None,
                                    Some(DecimalTarget),
                                    1
                                )
                            ),
                            2
                        )
                    )
                )
            )
        )
    )
)
