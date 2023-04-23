package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   PerspectiveInlineDerive.deriveDecoder
}
