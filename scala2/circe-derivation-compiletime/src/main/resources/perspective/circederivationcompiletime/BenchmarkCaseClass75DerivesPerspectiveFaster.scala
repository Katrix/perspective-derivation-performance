package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   PerspectiveFasterDerive.deriveDecoder
}
