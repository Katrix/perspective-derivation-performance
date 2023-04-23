package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   Shapeless2Derive.deriveDecoder
}
