package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass50DerivesPerspective {
  import perspective.circederivation.PerspectiveDerive

  implicit val encoder: Encoder[BenchmarkCaseClass50] =   PerspectiveDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass50] =   PerspectiveDerive.deriveDecoder
}
