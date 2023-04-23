package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass23DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass23] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass23] =   PerspectiveUnrollingDerive.deriveDecoder
}
