package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   PerspectiveDerive.deriveDecoder
}
