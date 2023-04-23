package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass5DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass5] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass5] =   PerspectiveUnrollingDerive.deriveDecoder
}
