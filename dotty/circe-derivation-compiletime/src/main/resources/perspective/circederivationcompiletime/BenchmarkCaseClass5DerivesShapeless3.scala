package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   Shapeless3Derive.deriveDecoder
}
