package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass22DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass22] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass22] =   PerspectiveUnrollingDerive.deriveDecoder
}
