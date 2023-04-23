package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   PerspectiveFasterDerive.deriveDecoder
}
