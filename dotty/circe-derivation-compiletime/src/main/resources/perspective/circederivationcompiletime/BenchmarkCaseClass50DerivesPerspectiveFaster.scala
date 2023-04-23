package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   PerspectiveFasterDerive.deriveDecoder
}
