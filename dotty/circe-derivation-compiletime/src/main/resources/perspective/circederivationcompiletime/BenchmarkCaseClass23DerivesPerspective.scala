package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   PerspectiveDerive.deriveDecoder
}
