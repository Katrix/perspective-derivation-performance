package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesHandwritten {
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
