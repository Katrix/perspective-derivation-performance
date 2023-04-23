package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesShapeless3 {
  import perspective.circederivation.Shapeless3Derive

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   Shapeless3Derive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   Shapeless3Derive.deriveDecoder
}
