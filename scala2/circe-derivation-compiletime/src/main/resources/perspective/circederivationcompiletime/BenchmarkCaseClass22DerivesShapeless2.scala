package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   Shapeless2Derive.deriveDecoder
}
