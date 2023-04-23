package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   Shapeless2Derive.deriveDecoder
}
