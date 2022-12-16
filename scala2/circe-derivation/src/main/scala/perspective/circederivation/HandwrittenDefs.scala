package perspective.circederivation

import io.circe._
import io.circe.syntax._
import org.openjdk.jmh.annotations._
import scala.annotation.switch
import scala.util.Random

object HandwrittenDefs extends BenchmarkDefs {

  case class BenchmarkCaseClass5(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json)
  object BenchmarkCaseClass5 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass5] =   new Encoder[BenchmarkCaseClass5] {
      override def apply(a: BenchmarkCaseClass5): Json = {
        Json.obj(
          "f0" := a.f0.asJson, "f1" := a.f1.asJson, "f2" := a.f2.asJson, "f3" := a.f3.asJson, "f4" := a.f4.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass5] =   new Decoder[BenchmarkCaseClass5] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass5] = {
        for {
          f0 <- cursor.get[Int]("f0")
          f1 <- cursor.get[String]("f1")
          f2 <- cursor.get[Double]("f2")
          f3 <- cursor.get[Boolean]("f3")
          f4 <- cursor.get[Json]("f4")
        } yield BenchmarkCaseClass5(f0, f1, f2, f3, f4)
      }
    }
  }

  def encode(intId: Int, obj: AnyRef): Json = (intId: @switch) match {
    case 5 => obj.asInstanceOf[BenchmarkCaseClass5].asJson
  }

  def decode(intId: Int, json: Json): AnyRef = (intId: @switch) match {
    case 5 => json.as[BenchmarkCaseClass5]
  }

  @State(Scope.Benchmark)
  class EncodeData {
    var vBenchmarkCaseClass5: BenchmarkCaseClass5 = null
    
    @Param(Array("5"))
    var intId = 0

    def getData(id: Int): AnyRef = (id: @switch) match {
      case 5 => vBenchmarkCaseClass5
    }
    
    @Setup
    def setup(): Unit = {
      val rand: Random = new Random(1234)
      vBenchmarkCaseClass5 = BenchmarkCaseClass5(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand))
    }
  }
  
  @State(Scope.Benchmark)
  class DecodeData {
    var sBenchmarkCaseClass5: Json = null
    
    @Param(Array("5"))
    var intId = 0

    def getData(id: Int): Json = (id: @switch) match {
      case 5 => sBenchmarkCaseClass5
    }
    
    @Setup
    def setup(): Unit = {
      val rand: Random = new Random(1234)
      sBenchmarkCaseClass5 = BenchmarkCaseClass5(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand)).asJson
    }
  }
}
