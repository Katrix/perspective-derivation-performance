package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass75DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass75] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass75] =   PerspectiveUnrollingDerive.deriveDecoder
}
