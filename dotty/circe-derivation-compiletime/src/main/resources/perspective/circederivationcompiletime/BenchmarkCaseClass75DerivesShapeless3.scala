package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   Shapeless3Derive.deriveDecoder
}
