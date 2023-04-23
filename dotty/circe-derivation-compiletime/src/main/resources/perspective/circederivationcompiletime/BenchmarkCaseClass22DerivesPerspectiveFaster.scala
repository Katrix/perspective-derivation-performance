package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   PerspectiveFasterDerive.deriveDecoder
}
