package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass10DerivesPerspectiveInline {
  import perspective.circederivation.PerspectiveInlineDerive

  implicit val encoder: Encoder[BenchmarkCaseClass10] =   PerspectiveInlineDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass10] =   PerspectiveInlineDerive.deriveDecoder
}
