package perspective.circederivation

import io.circe._
import io.circe.syntax._
import org.openjdk.jmh.annotations._
import scala.annotation.switch
import scala.util.Random

object HandwrittenDefs extends BenchmarkDefs {

  case class BenchmarkCaseClass1(f0: Int)
  object BenchmarkCaseClass1 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass1] =   new Encoder[BenchmarkCaseClass1] {
      override def apply(a: BenchmarkCaseClass1): Json = {
        Json.obj(
          "f0" := a.f0.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass1] =   new Decoder[BenchmarkCaseClass1] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass1] = {
        for {
          f0 <- cursor.get[Int]("f0")
        } yield BenchmarkCaseClass1(f0)
      }
    }
  }
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
  case class BenchmarkCaseClass10(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json)
  object BenchmarkCaseClass10 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass10] =   new Encoder[BenchmarkCaseClass10] {
      override def apply(a: BenchmarkCaseClass10): Json = {
        Json.obj(
          "f0" := a.f0.asJson, "f1" := a.f1.asJson, "f2" := a.f2.asJson, "f3" := a.f3.asJson, "f4" := a.f4.asJson, "f5" := a.f5.asJson, "f6" := a.f6.asJson, "f7" := a.f7.asJson, "f8" := a.f8.asJson, "f9" := a.f9.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass10] =   new Decoder[BenchmarkCaseClass10] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass10] = {
        for {
          f0 <- cursor.get[Int]("f0")
          f1 <- cursor.get[String]("f1")
          f2 <- cursor.get[Double]("f2")
          f3 <- cursor.get[Boolean]("f3")
          f4 <- cursor.get[Json]("f4")
          f5 <- cursor.get[Int]("f5")
          f6 <- cursor.get[String]("f6")
          f7 <- cursor.get[Double]("f7")
          f8 <- cursor.get[Boolean]("f8")
          f9 <- cursor.get[Json]("f9")
        } yield BenchmarkCaseClass10(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9)
      }
    }
  }
  case class BenchmarkCaseClass22(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String)
  object BenchmarkCaseClass22 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass22] =   new Encoder[BenchmarkCaseClass22] {
      override def apply(a: BenchmarkCaseClass22): Json = {
        Json.obj(
          "f0" := a.f0.asJson, "f1" := a.f1.asJson, "f2" := a.f2.asJson, "f3" := a.f3.asJson, "f4" := a.f4.asJson, "f5" := a.f5.asJson, "f6" := a.f6.asJson, "f7" := a.f7.asJson, "f8" := a.f8.asJson, "f9" := a.f9.asJson, "f10" := a.f10.asJson, "f11" := a.f11.asJson, "f12" := a.f12.asJson, "f13" := a.f13.asJson, "f14" := a.f14.asJson, "f15" := a.f15.asJson, "f16" := a.f16.asJson, "f17" := a.f17.asJson, "f18" := a.f18.asJson, "f19" := a.f19.asJson, "f20" := a.f20.asJson, "f21" := a.f21.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass22] =   new Decoder[BenchmarkCaseClass22] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass22] = {
        for {
          f0 <- cursor.get[Int]("f0")
          f1 <- cursor.get[String]("f1")
          f2 <- cursor.get[Double]("f2")
          f3 <- cursor.get[Boolean]("f3")
          f4 <- cursor.get[Json]("f4")
          f5 <- cursor.get[Int]("f5")
          f6 <- cursor.get[String]("f6")
          f7 <- cursor.get[Double]("f7")
          f8 <- cursor.get[Boolean]("f8")
          f9 <- cursor.get[Json]("f9")
          f10 <- cursor.get[Int]("f10")
          f11 <- cursor.get[String]("f11")
          f12 <- cursor.get[Double]("f12")
          f13 <- cursor.get[Boolean]("f13")
          f14 <- cursor.get[Json]("f14")
          f15 <- cursor.get[Int]("f15")
          f16 <- cursor.get[String]("f16")
          f17 <- cursor.get[Double]("f17")
          f18 <- cursor.get[Boolean]("f18")
          f19 <- cursor.get[Json]("f19")
          f20 <- cursor.get[Int]("f20")
          f21 <- cursor.get[String]("f21")
        } yield BenchmarkCaseClass22(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21)
      }
    }
  }
  case class BenchmarkCaseClass23(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String, f22: Double)
  object BenchmarkCaseClass23 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass23] =   new Encoder[BenchmarkCaseClass23] {
      override def apply(a: BenchmarkCaseClass23): Json = {
        Json.obj(
          "f0" := a.f0.asJson, "f1" := a.f1.asJson, "f2" := a.f2.asJson, "f3" := a.f3.asJson, "f4" := a.f4.asJson, "f5" := a.f5.asJson, "f6" := a.f6.asJson, "f7" := a.f7.asJson, "f8" := a.f8.asJson, "f9" := a.f9.asJson, "f10" := a.f10.asJson, "f11" := a.f11.asJson, "f12" := a.f12.asJson, "f13" := a.f13.asJson, "f14" := a.f14.asJson, "f15" := a.f15.asJson, "f16" := a.f16.asJson, "f17" := a.f17.asJson, "f18" := a.f18.asJson, "f19" := a.f19.asJson, "f20" := a.f20.asJson, "f21" := a.f21.asJson, "f22" := a.f22.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass23] =   new Decoder[BenchmarkCaseClass23] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass23] = {
        for {
          f0 <- cursor.get[Int]("f0")
          f1 <- cursor.get[String]("f1")
          f2 <- cursor.get[Double]("f2")
          f3 <- cursor.get[Boolean]("f3")
          f4 <- cursor.get[Json]("f4")
          f5 <- cursor.get[Int]("f5")
          f6 <- cursor.get[String]("f6")
          f7 <- cursor.get[Double]("f7")
          f8 <- cursor.get[Boolean]("f8")
          f9 <- cursor.get[Json]("f9")
          f10 <- cursor.get[Int]("f10")
          f11 <- cursor.get[String]("f11")
          f12 <- cursor.get[Double]("f12")
          f13 <- cursor.get[Boolean]("f13")
          f14 <- cursor.get[Json]("f14")
          f15 <- cursor.get[Int]("f15")
          f16 <- cursor.get[String]("f16")
          f17 <- cursor.get[Double]("f17")
          f18 <- cursor.get[Boolean]("f18")
          f19 <- cursor.get[Json]("f19")
          f20 <- cursor.get[Int]("f20")
          f21 <- cursor.get[String]("f21")
          f22 <- cursor.get[Double]("f22")
        } yield BenchmarkCaseClass23(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22)
      }
    }
  }
  case class BenchmarkCaseClass50(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String, f22: Double, f23: Boolean, f24: Json, f25: Int, f26: String, f27: Double, f28: Boolean, f29: Json, f30: Int, f31: String, f32: Double, f33: Boolean, f34: Json, f35: Int, f36: String, f37: Double, f38: Boolean, f39: Json, f40: Int, f41: String, f42: Double, f43: Boolean, f44: Json, f45: Int, f46: String, f47: Double, f48: Boolean, f49: Json)
  object BenchmarkCaseClass50 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass50] =   new Encoder[BenchmarkCaseClass50] {
      override def apply(a: BenchmarkCaseClass50): Json = {
        Json.obj(
          "f0" := a.f0.asJson, "f1" := a.f1.asJson, "f2" := a.f2.asJson, "f3" := a.f3.asJson, "f4" := a.f4.asJson, "f5" := a.f5.asJson, "f6" := a.f6.asJson, "f7" := a.f7.asJson, "f8" := a.f8.asJson, "f9" := a.f9.asJson, "f10" := a.f10.asJson, "f11" := a.f11.asJson, "f12" := a.f12.asJson, "f13" := a.f13.asJson, "f14" := a.f14.asJson, "f15" := a.f15.asJson, "f16" := a.f16.asJson, "f17" := a.f17.asJson, "f18" := a.f18.asJson, "f19" := a.f19.asJson, "f20" := a.f20.asJson, "f21" := a.f21.asJson, "f22" := a.f22.asJson, "f23" := a.f23.asJson, "f24" := a.f24.asJson, "f25" := a.f25.asJson, "f26" := a.f26.asJson, "f27" := a.f27.asJson, "f28" := a.f28.asJson, "f29" := a.f29.asJson, "f30" := a.f30.asJson, "f31" := a.f31.asJson, "f32" := a.f32.asJson, "f33" := a.f33.asJson, "f34" := a.f34.asJson, "f35" := a.f35.asJson, "f36" := a.f36.asJson, "f37" := a.f37.asJson, "f38" := a.f38.asJson, "f39" := a.f39.asJson, "f40" := a.f40.asJson, "f41" := a.f41.asJson, "f42" := a.f42.asJson, "f43" := a.f43.asJson, "f44" := a.f44.asJson, "f45" := a.f45.asJson, "f46" := a.f46.asJson, "f47" := a.f47.asJson, "f48" := a.f48.asJson, "f49" := a.f49.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass50] =   new Decoder[BenchmarkCaseClass50] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass50] = {
        for {
          f0 <- cursor.get[Int]("f0")
          f1 <- cursor.get[String]("f1")
          f2 <- cursor.get[Double]("f2")
          f3 <- cursor.get[Boolean]("f3")
          f4 <- cursor.get[Json]("f4")
          f5 <- cursor.get[Int]("f5")
          f6 <- cursor.get[String]("f6")
          f7 <- cursor.get[Double]("f7")
          f8 <- cursor.get[Boolean]("f8")
          f9 <- cursor.get[Json]("f9")
          f10 <- cursor.get[Int]("f10")
          f11 <- cursor.get[String]("f11")
          f12 <- cursor.get[Double]("f12")
          f13 <- cursor.get[Boolean]("f13")
          f14 <- cursor.get[Json]("f14")
          f15 <- cursor.get[Int]("f15")
          f16 <- cursor.get[String]("f16")
          f17 <- cursor.get[Double]("f17")
          f18 <- cursor.get[Boolean]("f18")
          f19 <- cursor.get[Json]("f19")
          f20 <- cursor.get[Int]("f20")
          f21 <- cursor.get[String]("f21")
          f22 <- cursor.get[Double]("f22")
          f23 <- cursor.get[Boolean]("f23")
          f24 <- cursor.get[Json]("f24")
          f25 <- cursor.get[Int]("f25")
          f26 <- cursor.get[String]("f26")
          f27 <- cursor.get[Double]("f27")
          f28 <- cursor.get[Boolean]("f28")
          f29 <- cursor.get[Json]("f29")
          f30 <- cursor.get[Int]("f30")
          f31 <- cursor.get[String]("f31")
          f32 <- cursor.get[Double]("f32")
          f33 <- cursor.get[Boolean]("f33")
          f34 <- cursor.get[Json]("f34")
          f35 <- cursor.get[Int]("f35")
          f36 <- cursor.get[String]("f36")
          f37 <- cursor.get[Double]("f37")
          f38 <- cursor.get[Boolean]("f38")
          f39 <- cursor.get[Json]("f39")
          f40 <- cursor.get[Int]("f40")
          f41 <- cursor.get[String]("f41")
          f42 <- cursor.get[Double]("f42")
          f43 <- cursor.get[Boolean]("f43")
          f44 <- cursor.get[Json]("f44")
          f45 <- cursor.get[Int]("f45")
          f46 <- cursor.get[String]("f46")
          f47 <- cursor.get[Double]("f47")
          f48 <- cursor.get[Boolean]("f48")
          f49 <- cursor.get[Json]("f49")
        } yield BenchmarkCaseClass50(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31, f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49)
      }
    }
  }
  case class BenchmarkCaseClass75(f0: Int, f1: String, f2: Double, f3: Boolean, f4: Json, f5: Int, f6: String, f7: Double, f8: Boolean, f9: Json, f10: Int, f11: String, f12: Double, f13: Boolean, f14: Json, f15: Int, f16: String, f17: Double, f18: Boolean, f19: Json, f20: Int, f21: String, f22: Double, f23: Boolean, f24: Json, f25: Int, f26: String, f27: Double, f28: Boolean, f29: Json, f30: Int, f31: String, f32: Double, f33: Boolean, f34: Json, f35: Int, f36: String, f37: Double, f38: Boolean, f39: Json, f40: Int, f41: String, f42: Double, f43: Boolean, f44: Json, f45: Int, f46: String, f47: Double, f48: Boolean, f49: Json, f50: Int, f51: String, f52: Double, f53: Boolean, f54: Json, f55: Int, f56: String, f57: Double, f58: Boolean, f59: Json, f60: Int, f61: String, f62: Double, f63: Boolean, f64: Json, f65: Int, f66: String, f67: Double, f68: Boolean, f69: Json, f70: Int, f71: String, f72: Double, f73: Boolean, f74: Json)
  object BenchmarkCaseClass75 {
    import io.circe.syntax._
  
    implicit val encoder: Encoder[BenchmarkCaseClass75] =   new Encoder[BenchmarkCaseClass75] {
      override def apply(a: BenchmarkCaseClass75): Json = {
        Json.obj(
          "f0" := a.f0.asJson, "f1" := a.f1.asJson, "f2" := a.f2.asJson, "f3" := a.f3.asJson, "f4" := a.f4.asJson, "f5" := a.f5.asJson, "f6" := a.f6.asJson, "f7" := a.f7.asJson, "f8" := a.f8.asJson, "f9" := a.f9.asJson, "f10" := a.f10.asJson, "f11" := a.f11.asJson, "f12" := a.f12.asJson, "f13" := a.f13.asJson, "f14" := a.f14.asJson, "f15" := a.f15.asJson, "f16" := a.f16.asJson, "f17" := a.f17.asJson, "f18" := a.f18.asJson, "f19" := a.f19.asJson, "f20" := a.f20.asJson, "f21" := a.f21.asJson, "f22" := a.f22.asJson, "f23" := a.f23.asJson, "f24" := a.f24.asJson, "f25" := a.f25.asJson, "f26" := a.f26.asJson, "f27" := a.f27.asJson, "f28" := a.f28.asJson, "f29" := a.f29.asJson, "f30" := a.f30.asJson, "f31" := a.f31.asJson, "f32" := a.f32.asJson, "f33" := a.f33.asJson, "f34" := a.f34.asJson, "f35" := a.f35.asJson, "f36" := a.f36.asJson, "f37" := a.f37.asJson, "f38" := a.f38.asJson, "f39" := a.f39.asJson, "f40" := a.f40.asJson, "f41" := a.f41.asJson, "f42" := a.f42.asJson, "f43" := a.f43.asJson, "f44" := a.f44.asJson, "f45" := a.f45.asJson, "f46" := a.f46.asJson, "f47" := a.f47.asJson, "f48" := a.f48.asJson, "f49" := a.f49.asJson, "f50" := a.f50.asJson, "f51" := a.f51.asJson, "f52" := a.f52.asJson, "f53" := a.f53.asJson, "f54" := a.f54.asJson, "f55" := a.f55.asJson, "f56" := a.f56.asJson, "f57" := a.f57.asJson, "f58" := a.f58.asJson, "f59" := a.f59.asJson, "f60" := a.f60.asJson, "f61" := a.f61.asJson, "f62" := a.f62.asJson, "f63" := a.f63.asJson, "f64" := a.f64.asJson, "f65" := a.f65.asJson, "f66" := a.f66.asJson, "f67" := a.f67.asJson, "f68" := a.f68.asJson, "f69" := a.f69.asJson, "f70" := a.f70.asJson, "f71" := a.f71.asJson, "f72" := a.f72.asJson, "f73" := a.f73.asJson, "f74" := a.f74.asJson
        )
      }
    }
    implicit val decoder: Decoder[BenchmarkCaseClass75] =   new Decoder[BenchmarkCaseClass75] {
      override def apply(cursor: HCursor): Decoder.Result[BenchmarkCaseClass75] = {
        for {
          f0 <- cursor.get[Int]("f0")
          f1 <- cursor.get[String]("f1")
          f2 <- cursor.get[Double]("f2")
          f3 <- cursor.get[Boolean]("f3")
          f4 <- cursor.get[Json]("f4")
          f5 <- cursor.get[Int]("f5")
          f6 <- cursor.get[String]("f6")
          f7 <- cursor.get[Double]("f7")
          f8 <- cursor.get[Boolean]("f8")
          f9 <- cursor.get[Json]("f9")
          f10 <- cursor.get[Int]("f10")
          f11 <- cursor.get[String]("f11")
          f12 <- cursor.get[Double]("f12")
          f13 <- cursor.get[Boolean]("f13")
          f14 <- cursor.get[Json]("f14")
          f15 <- cursor.get[Int]("f15")
          f16 <- cursor.get[String]("f16")
          f17 <- cursor.get[Double]("f17")
          f18 <- cursor.get[Boolean]("f18")
          f19 <- cursor.get[Json]("f19")
          f20 <- cursor.get[Int]("f20")
          f21 <- cursor.get[String]("f21")
          f22 <- cursor.get[Double]("f22")
          f23 <- cursor.get[Boolean]("f23")
          f24 <- cursor.get[Json]("f24")
          f25 <- cursor.get[Int]("f25")
          f26 <- cursor.get[String]("f26")
          f27 <- cursor.get[Double]("f27")
          f28 <- cursor.get[Boolean]("f28")
          f29 <- cursor.get[Json]("f29")
          f30 <- cursor.get[Int]("f30")
          f31 <- cursor.get[String]("f31")
          f32 <- cursor.get[Double]("f32")
          f33 <- cursor.get[Boolean]("f33")
          f34 <- cursor.get[Json]("f34")
          f35 <- cursor.get[Int]("f35")
          f36 <- cursor.get[String]("f36")
          f37 <- cursor.get[Double]("f37")
          f38 <- cursor.get[Boolean]("f38")
          f39 <- cursor.get[Json]("f39")
          f40 <- cursor.get[Int]("f40")
          f41 <- cursor.get[String]("f41")
          f42 <- cursor.get[Double]("f42")
          f43 <- cursor.get[Boolean]("f43")
          f44 <- cursor.get[Json]("f44")
          f45 <- cursor.get[Int]("f45")
          f46 <- cursor.get[String]("f46")
          f47 <- cursor.get[Double]("f47")
          f48 <- cursor.get[Boolean]("f48")
          f49 <- cursor.get[Json]("f49")
          f50 <- cursor.get[Int]("f50")
          f51 <- cursor.get[String]("f51")
          f52 <- cursor.get[Double]("f52")
          f53 <- cursor.get[Boolean]("f53")
          f54 <- cursor.get[Json]("f54")
          f55 <- cursor.get[Int]("f55")
          f56 <- cursor.get[String]("f56")
          f57 <- cursor.get[Double]("f57")
          f58 <- cursor.get[Boolean]("f58")
          f59 <- cursor.get[Json]("f59")
          f60 <- cursor.get[Int]("f60")
          f61 <- cursor.get[String]("f61")
          f62 <- cursor.get[Double]("f62")
          f63 <- cursor.get[Boolean]("f63")
          f64 <- cursor.get[Json]("f64")
          f65 <- cursor.get[Int]("f65")
          f66 <- cursor.get[String]("f66")
          f67 <- cursor.get[Double]("f67")
          f68 <- cursor.get[Boolean]("f68")
          f69 <- cursor.get[Json]("f69")
          f70 <- cursor.get[Int]("f70")
          f71 <- cursor.get[String]("f71")
          f72 <- cursor.get[Double]("f72")
          f73 <- cursor.get[Boolean]("f73")
          f74 <- cursor.get[Json]("f74")
        } yield BenchmarkCaseClass75(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, f29, f30, f31, f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42, f43, f44, f45, f46, f47, f48, f49, f50, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63, f64, f65, f66, f67, f68, f69, f70, f71, f72, f73, f74)
      }
    }
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
