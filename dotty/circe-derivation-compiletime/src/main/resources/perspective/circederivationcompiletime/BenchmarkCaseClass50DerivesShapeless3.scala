package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   Shapeless3Derive.deriveDecoder
}
