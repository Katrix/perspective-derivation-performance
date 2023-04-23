package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesHandwritten {
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
