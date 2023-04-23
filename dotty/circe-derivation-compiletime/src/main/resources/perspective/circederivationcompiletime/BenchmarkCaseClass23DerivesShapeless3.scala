package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   Shapeless3Derive.deriveDecoder
}
