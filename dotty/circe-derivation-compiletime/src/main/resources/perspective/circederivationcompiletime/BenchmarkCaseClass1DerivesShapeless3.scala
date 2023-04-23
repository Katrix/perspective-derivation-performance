package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   Shapeless3Derive.deriveDecoder
}
