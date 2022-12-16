package perspective.circederivation

import io.circe._
import io.circe.syntax._
import org.openjdk.jmh.annotations._
import scala.annotation.switch
import scala.util.Random

object CirceDerivationDefs extends BenchmarkDefs {

  case class BenchmarkCaseClass5(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json)
  object BenchmarkCaseClass5 {
    import io.circe.derivation._
  
    implicit val encoder: Encoder[BenchmarkCaseClass5] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass5] =   deriveDecoder
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
