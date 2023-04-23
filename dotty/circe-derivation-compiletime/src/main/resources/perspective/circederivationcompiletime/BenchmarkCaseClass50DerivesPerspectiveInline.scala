package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   PerspectiveInlineDerive.deriveDecoder
}
