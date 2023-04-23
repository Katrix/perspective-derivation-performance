package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesHandwritten {
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
