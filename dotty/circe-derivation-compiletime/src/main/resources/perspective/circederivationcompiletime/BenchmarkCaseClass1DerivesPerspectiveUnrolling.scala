package perspective.circederivationcompiletime

import io.circe._
object BenchmarkCaseClass1DerivesPerspectiveUnrolling {
  import perspective.circederivation.PerspectiveUnrollingDerive

  implicit val encoder: Encoder[BenchmarkCaseClass1] =   PerspectiveUnrollingDerive.deriveEncoder
  implicit val decoder: Decoder[BenchmarkCaseClass1] =   PerspectiveUnrollingDerive.deriveDecoder
}
