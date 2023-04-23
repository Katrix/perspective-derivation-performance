package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   Shapeless2Derive.deriveDecoder
}
