package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   PerspectiveInlineDerive.deriveDecoder
}
