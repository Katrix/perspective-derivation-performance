package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   PerspectiveDerive.deriveDecoder
}
