package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   PerspectiveDerive.deriveDecoder
}
