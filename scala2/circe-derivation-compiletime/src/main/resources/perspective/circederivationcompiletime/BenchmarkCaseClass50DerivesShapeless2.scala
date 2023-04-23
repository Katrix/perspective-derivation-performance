package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesShapeless2 {
  import perspective.circederivation.Shapeless2Derive

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   Shapeless2Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   Shapeless2Derive.deriveDecoder
}
