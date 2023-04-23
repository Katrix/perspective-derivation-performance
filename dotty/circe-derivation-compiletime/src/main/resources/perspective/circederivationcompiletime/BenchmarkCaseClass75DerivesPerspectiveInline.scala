package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   PerspectiveInlineDerive.deriveDecoder
}
