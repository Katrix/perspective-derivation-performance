package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   PerspectiveInlineDerive.deriveDecoder
}
