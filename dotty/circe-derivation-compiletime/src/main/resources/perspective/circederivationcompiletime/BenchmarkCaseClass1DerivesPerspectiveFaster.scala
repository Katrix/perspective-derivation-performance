package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesPerspectiveFaster {
  import perspective.circederivation.PerspectiveFasterDerive

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   PerspectiveFasterDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   PerspectiveFasterDerive.deriveDecoder
}
