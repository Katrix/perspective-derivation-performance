package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   PerspectiveFasterDerive.deriveDecoder
}
