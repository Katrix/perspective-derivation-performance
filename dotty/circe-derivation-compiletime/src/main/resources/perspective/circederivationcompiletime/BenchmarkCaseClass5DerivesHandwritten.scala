package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesHandwritten {
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
