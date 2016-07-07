
import org.scalatest.{Matchers, FunSuite}

class MyTest extends FunSuite with Matchers

class MatchingSingle1 extends MyTest {
    val dom = new HTMLParser().fromUrl("/Users/marcin/projects/scala/livescrape/src/main/resources/goog_gdxj.html")
    val tp = new TargetParser()

    val reprDecimalT = "div@class:snap-panel-and-plusone.div@class:snap-panel.table@class:snap-data[0].tr[2].td[1]!decimal"
    val decimalT = tp(reprDecimalT).get
    val decimalRes = Resolver(decimalT, dom)

    test("Target value should be Double") {
        decimalRes(0) shouldBe a [java.lang.Double]
    }

    test("Target should have value 40.43") {
        decimalRes(0) should === (40.43)
    }

    val reprStrT = "div@class:snap-panel-and-plusone.div@class:snap-panel.table@class:snap-data[0].tr[2].td[1]!str"
    val strT = tp(reprStrT).get
    val strRes = Resolver(strT, dom)

    test("Target value should be String") {
        strRes(0) shouldBe a [String]
    }

    test("Target should have value equal to 40.43") {
        strRes(0) should === ("40.43")
    }

    val reprIntT = "div@class:snap-panel-and-plusone.div@class:snap-panel.table@class:snap-data[0].tr[2].td[1]!int"
    val intT = tp(reprIntT).get
    val intRes = Resolver(intT, dom)

    test("Target value should be Int") {
        intRes(0) shouldBe a [java.lang.Integer]
    }

    test("Target should have value equal to 40") {
        intRes(0) should === (40)
    }
}

class MatchingSingle2 extends MyTest {
    val dom = new HTMLParser().fromUrl("/Users/marcin/projects/scala/livescrape/src/main/resources/test.html")
    val tp = new TargetParser()

    val tag1 = tp("div@id:products.div@id:product[0].p@class:price!decimal").get
    val res1 = Resolver(tag1, dom)

    test("Target should be only one match") {
        res1.length shouldBe 1
    }

    test("Target should be a Double with value of 14.10") {
        res1(0) should === (14.10)
    }

    val tag2 = tp("div@id:products.div@id:product[1].p@class:price!decimal").get
    val res2 = Resolver(tag2, dom)

    test("Target should be a Double with value of 15.10") {
        res2(0) should === (15.10)
    }
}

class MatchingMultiple extends MyTest {
    val dom = new HTMLParser().fromUrl("/Users/marcin/projects/scala/livescrape/src/main/resources/test.html")
    val tp = new TargetParser()

    val tag = tp("div@id:products.div@id:product.p@class:price!decimal").get
    val res = Resolver(tag, dom)

    test("Target should be a List[Double] value of [14.10, 15.10]") {
        res should === (List(14.10, 15.10))
    }
}

class Soccer extends MyTest {
    val dom = new HTMLParser().fromUrl("http://www.espnfc.us/scores")
    val tp = new TargetParser()

    val tag = tp("div@class:team-score.span!int").get
    val res = Resolver(tag, dom)

    test("Target should be a") {
        res should === (List(0, 0))
    }
}

