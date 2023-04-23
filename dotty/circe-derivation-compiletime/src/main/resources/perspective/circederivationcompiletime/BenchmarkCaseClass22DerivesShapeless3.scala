package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   Shapeless3Derive.deriveDecoder
}