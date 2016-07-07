import scala.xml._
// Document to parse
val dom = HTMLParser.fromUrl("https://www.google.com/finance?q=gdxj")
// Target location in the document
val s = "div@class:snap-panel-and-plusone.div@class:snap-panel.table@class:span-data.tr[2].td[1]!decimal"
val tp = new TargetParser()
val tloc = tp(s)
def resolve(tloc: TargetVal, dom: Seq[Node]): Any = {
    tloc match {
        case t: TagTarget => t.value match {
            case v: TagTarget => v.attr match {
                case Some(a) => {
                    if (v.idx.isInstanceOf[Some[Int]]) {
                        resolve(v.value, (dom.filter(_ \@ a.key == a.value))(v.idx.get))
                    } else {
                        resolve(v.value, dom.filter(_ \@ a.key == a.value))
                    }
                }
                case _ => {
                    if (v.idx.isInstanceOf[Some[Int]]) {
                        resolve(v.value, (dom \ v.name)(v.idx.get))
                    } else {
                        resolve(v.value, dom \ v.name)
                    }
                }
            }
            case _ => {
                dom
            }
        }
    }
}
val res = dom \ "body" \\ "div"
