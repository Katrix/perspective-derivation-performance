package perspective.circederivation

import io.circe._
import io.circe.syntax._
import org.openjdk.jmh.annotations._
import scala.annotation.switch
import scala.util.Random

object CirceGenericDefs extends BenchmarkDefs {

  case class BenchmarkCaseClass1(f0: Int)
  object BenchmarkCaseClass1 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass1] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass1] =   deriveDecoder
  }
  case class BenchmarkCaseClass5(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json)
  object BenchmarkCaseClass5 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass5] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass5] =   deriveDecoder
  }
  case class BenchmarkCaseClass10(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json)
  object BenchmarkCaseClass10 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass10] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass10] =   deriveDecoder
  }
  case class BenchmarkCaseClass22(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String)
  object BenchmarkCaseClass22 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass22] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass22] =   deriveDecoder
  }
  case class BenchmarkCaseClass23(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String, f22: Double)
  object BenchmarkCaseClass23 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass23] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass23] =   deriveDecoder
  }
  case class BenchmarkCaseClass50(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String, f22: Double, f23: Boolean, f24: Json, f25: Int, f26: String, f27: Double, f28: Boolean, f29: Json, f30: Int, f31: String, f32: Double, f33: Boolean, f34: Json, f35: Int, f36: String, f37: Double, f38: Boolean, f39: Json, f40: Int, f41: String, f42: Double, f43: Boolean, f44: Json, f45: Int, f46: String, f47: Double, f48: Boolean, f49: Json)
  object BenchmarkCaseClass50 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass50] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass50] =   deriveDecoder
  }
  case class BenchmarkCaseClass75(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String, f22: Double, f23: Boolean, f24: Json, f25: Int, f26: String, f27: Double, f28: Boolean, f29: Json, f30: Int, f31: String, f32: Double, f33: Boolean, f34: Json, f35: Int, f36: String, f37: Double, f38: Boolean, f39: Json, f40: Int, f41: String, f42: Double, f43: Boolean, f44: Json, f45: Int, f46: String, f47: Double, f48: Boolean, f49: Json, f50: Int, f51: String, f52: Double, f53: Boolean, f54: Json, f55: Int, f56: String, f57: Double, f58: Boolean, f59: Json, f60: Int, f61: String, f62: Double, f63: Boolean, f64: Json, f65: Int, f66: String, f67: Double, f68: Boolean, f69: Json, f70: Int, f71: String, f72: Double, f73: Boolean, f74: Json)
  object BenchmarkCaseClass75 {
    import io.circe.generic.semiauto._
  
    implicit val encoder: Encoder[BenchmarkCaseClass75] =   deriveEncoder
    implicit val decoder: Decoder[BenchmarkCaseClass75] =   deriveDecoder
  }

  def encode(intId: Int, obj: AnyRef): Json = (intId: @switch) match {
    case 1 => obj.asInstanceOf[BenchmarkCaseClass1].asJson
    case 5 => obj.asInstanceOf[BenchmarkCaseClass5].asJson
    case 10 => obj.asInstanceOf[BenchmarkCaseClass10].asJson
    case 22 => obj.asInstanceOf[BenchmarkCaseClass22].asJson
    case 23 => obj.asInstanceOf[BenchmarkCaseClass23].asJson
    case 50 => obj.asInstanceOf[BenchmarkCaseClass50].asJson
    case 75 => obj.asInstanceOf[BenchmarkCaseClass75].asJson
  }

  def decode(intId: Int, json: Json): AnyRef = (intId: @switch) match {
    case 1 => json.as[BenchmarkCaseClass1]
    case 5 => json.as[BenchmarkCaseClass5]
    case 10 => json.as[BenchmarkCaseClass10]
    case 22 => json.as[BenchmarkCaseClass22]
    case 23 => json.as[BenchmarkCaseClass23]
    case 50 => json.as[BenchmarkCaseClass50]
    case 75 => json.as[BenchmarkCaseClass75]
  }

  @State(Scope.Benchmark)
  class EncodeData {
    @Param(Array("1234"))
    var randSeed: Int = _

    var vBenchmarkCaseClass1: BenchmarkCaseClass1 = null
    var vBenchmarkCaseClass5: BenchmarkCaseClass5 = null
    var vBenchmarkCaseClass10: BenchmarkCaseClass10 = null
    var vBenchmarkCaseClass22: BenchmarkCaseClass22 = null
    var vBenchmarkCaseClass23: BenchmarkCaseClass23 = null
    var vBenchmarkCaseClass50: BenchmarkCaseClass50 = null
    var vBenchmarkCaseClass75: BenchmarkCaseClass75 = null
    
    @Param(Array("1", "5", "10", "22", "23", "50", "75"))
    var intId = 0

    def getData(id: Int): AnyRef = (id: @switch) match {
      case 1 => vBenchmarkCaseClass1
      case 5 => vBenchmarkCaseClass5
      case 10 => vBenchmarkCaseClass10
      case 22 => vBenchmarkCaseClass22
      case 23 => vBenchmarkCaseClass23
      case 50 => vBenchmarkCaseClass50
      case 75 => vBenchmarkCaseClass75
    }
    
    @Setup
    def setup(): Unit = {
      val rand: Random = new Random(randSeed)
      vBenchmarkCaseClass1 = BenchmarkCaseClass1(genField[Int](rand))
      vBenchmarkCaseClass5 = BenchmarkCaseClass5(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand))
      vBenchmarkCaseClass10 = BenchmarkCaseClass10(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand))
      vBenchmarkCaseClass22 = BenchmarkCaseClass22(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand))
      vBenchmarkCaseClass23 = BenchmarkCaseClass23(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand))
      vBenchmarkCaseClass50 = BenchmarkCaseClass50(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand))
      vBenchmarkCaseClass75 = BenchmarkCaseClass75(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand))
    }
  }
  
  @State(Scope.Benchmark)
  class DecodeData {
    @Param(Array("1234"))
    var randSeed: Int = _

    var sBenchmarkCaseClass1: Json = null
    var sBenchmarkCaseClass5: Json = null
    var sBenchmarkCaseClass10: Json = null
    var sBenchmarkCaseClass22: Json = null
    var sBenchmarkCaseClass23: Json = null
    var sBenchmarkCaseClass50: Json = null
    var sBenchmarkCaseClass75: Json = null
    
    @Param(Array("1", "5", "10", "22", "23", "50", "75"))
    var intId = 0

    def getData(id: Int): Json = (id: @switch) match {
      case 1 => sBenchmarkCaseClass1
      case 5 => sBenchmarkCaseClass5
      case 10 => sBenchmarkCaseClass10
      case 22 => sBenchmarkCaseClass22
      case 23 => sBenchmarkCaseClass23
      case 50 => sBenchmarkCaseClass50
      case 75 => sBenchmarkCaseClass75
    }
    
    @Setup
    def setup(): Unit = {
      val rand: Random = new Random(randSeed)
      sBenchmarkCaseClass1 = BenchmarkCaseClass1(genField[Int](rand)).asJson
      sBenchmarkCaseClass5 = BenchmarkCaseClass5(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand)).asJson
      sBenchmarkCaseClass10 = BenchmarkCaseClass10(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand)).asJson
      sBenchmarkCaseClass22 = BenchmarkCaseClass22(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand)).asJson
      sBenchmarkCaseClass23 = BenchmarkCaseClass23(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand)).asJson
      sBenchmarkCaseClass50 = BenchmarkCaseClass50(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand)).asJson
      sBenchmarkCaseClass75 = BenchmarkCaseClass75(genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand), genField[Int](rand), genField[String](rand), genField[Double](rand), genField[Boolean](rand), genField[Json](rand)).asJson
    }
  }
}
