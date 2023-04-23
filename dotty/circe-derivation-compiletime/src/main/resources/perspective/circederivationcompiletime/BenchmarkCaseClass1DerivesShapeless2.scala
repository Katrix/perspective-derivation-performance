package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   Shapeless2Derive.deriveDecoder
}
