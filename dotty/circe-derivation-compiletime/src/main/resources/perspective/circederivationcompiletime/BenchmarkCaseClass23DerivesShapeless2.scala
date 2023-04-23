package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   Shapeless2Derive.deriveDecoder
}
