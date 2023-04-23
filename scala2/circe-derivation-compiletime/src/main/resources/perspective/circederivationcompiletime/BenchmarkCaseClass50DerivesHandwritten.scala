package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesHandwritten {
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
