package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   PerspectiveInlineDerive.deriveDecoder
}
