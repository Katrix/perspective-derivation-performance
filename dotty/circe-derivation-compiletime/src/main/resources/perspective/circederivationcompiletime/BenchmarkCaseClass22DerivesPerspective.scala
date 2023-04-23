package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   PerspectiveDerive.deriveDecoder
}
